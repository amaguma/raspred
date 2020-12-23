package lab6;

import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.AllDirectives;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletionStage;

public class App extends AllDirectives {
    private Http http;
    private ActorRef storeActor;
    private int port;

    

    private static CompletionStage<HttpResponse> fetch(String url) {
        return
    }
}
