package lab6;

import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.AllDirectives;

public class App extends AllDirectives {
    private Http http;
    private ActorRef storeActor;
    private int port;
}
