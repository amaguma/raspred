package lab6;

import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;

import java.util.concurrent.CompletionStage;

public class App extends AllDirectives {
    private static Http http;
    private ActorRef storeActor;
    private int port;


    private static CompletionStage<HttpResponse> fetch(String url) {
        return http.singleRequest(HttpRequest.create(url));
    }

    public Route createRoute() {
        return route(get(() ->
                    parameter("url", url ->
                            parameter("count", count -> {
                                if (Integer.parseInt(count) == 0) {
                                    return completeWithFuture(fetch(url));
                                } else {
                                    return completeWithFuture()
                                }
                            }))
                ))
    }
}
