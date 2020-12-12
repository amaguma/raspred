package lab5;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.japi.Pair;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;




public class AkkaApplication {

    public static Flow<HttpRequest, HttpResponse, NotUsed> createFlow(ActorSystem system, ActorRef cache, ActorMaterializer materializer) {
        return Flow.of(HttpRequest.class)
                .map(req -> {
                    Query query = req.getUri().query();
                    String url = query.get("testUrl").get();
                    int count = Integer.parseInt(query.get("count").get());
                    return new Pair<>(url, count);
                })
                .mapAsync(1, (pair) -> Patterns.ask(cache, pair.))
    }
}
