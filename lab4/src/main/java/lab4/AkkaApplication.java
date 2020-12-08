package lab4;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.server.AllDirectives;

public class AkkaApplication extends AllDirectives {
    private ActorRef actorRouter;

    private AkkaApplication(ActorRef actorRouter) {
        this.actorRouter = actorRouter;
    }

    public static void main(String[] args) throws  Exception {
        ActorSystem system = ActorSystem.create("")
    }
}
