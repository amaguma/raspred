package lab4;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.AllDirectives;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AkkaApplication extends AllDirectives {
    private ActorRef actorRouter;

    private AkkaApplication(ActorRef actorRouter) {
        this.actorRouter = actorRouter;
    }

    public static void main(String[] args) throws  Exception {
        ActorSystem system = ActorSystem.create("AkkaApplication");
        ActorRef actorRouter = system.actorOf(Props.create(RouterActor.class, system), "actorRouter");

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        AkkaApplication instance = new AkkaApplication(actorRouter);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = instance
    }
}
