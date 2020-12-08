package lab4;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import scala.concurrent.Future;

import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.PathMatchers.segment;

public class AkkaApplication extends AllDirectives {
    private ActorRef actorRouter;

    private AkkaApplication(ActorRef actorRouter) {
        this.actorRouter = actorRouter;
    }

    private Route createRoute(){
        return concat(
                get(() ->
                        pathPrefix("getPackage", () ->
                                path(segment(), (String id) -> {
                                            Future<Object> result = Patterns.ask(actorRouter, id, 3000);
                                            return completeOKWithFuture(result, Jackson.marshaller());
                                        }
                                ))),
                post(() ->
                        path("postPackage", () ->
                                entity(Jackson.unmarshaller(TestPackage.class), testPackage -> {
                                    actorRouter.tell(testPackage, ActorRef.noSender());
                                    return complete("Start tests");
                                })))
        );
    }

    public static void main(String[] args) throws  Exception {
        ActorSystem system = ActorSystem.create("AkkaApplication");
        ActorRef actorRouter = system.actorOf(Props.create(RouterActor.class, system), "actorRouter");

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        AkkaApplication instance = new AkkaApplication(actorRouter);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = instance.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8080), materializer);

        System.out.println("Server start at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();
        binding.thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());
    }
}
