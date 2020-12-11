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


public class AkkaApplication extends AllDirectives {

    private Route createRoute(ActorSystem system, ActorRef actorRouter){
        return concat(
                get(() -> parameter("packageId", id -> {
                    Future<Object> result = Patterns.ask(actorRouter, id, 3000);
                    return completeOKWithFuture(result, Jackson.marshaller());
                })),
                post(() -> entity(
                        Jackson.unmarshaller(TestPackage.class), testPackage -> {
                            actorRouter.tell(testPackage, ActorRef.noSender());
                                    return complete("Start tests");
                        }
                ))
        );
    }

    public static void main(String[] args) throws  Exception {
        ActorSystem system = ActorSystem.create("AkkaApplication");
        ActorRef actorRouter = system.actorOf(Props.create(RouterActor.class, system), "actorRouter");

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        final AkkaApplication instance = new AkkaApplication();

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = instance.createRoute(system, actorRouter).flow(system, materializer);

        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8080), materializer);


        System.out.println("Server start at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();
   
        binding.thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());
    }
}
