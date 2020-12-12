package lab5;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.japi.Pair;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;


import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.asynchttpclient.Dsl.asyncHttpClient;



public class AkkaApplication {

    public static Flow<HttpRequest, HttpResponse, NotUsed> createFlow(ActorSystem system, ActorRef cache, ActorMaterializer materializer) {
        return Flow.of(HttpRequest.class)
                .map(req -> {
                    Query query = req.getUri().query();
                    String url = query.get("testUrl").get();
                    int count = Integer.parseInt(query.get("count").get());
                    return new Pair<>(url, count);
                })
                .mapAsync(1, (pair) ->
                        Patterns.ask(cache, pair.first(), Duration.ofSeconds(5)).thenCompose(res -> {
                            if ((int)res >= 0) {
                                return CompletableFuture.completedFuture(new Pair<>(pair.first(), (int)res));
                            }
                            Flow<Pair<String, Integer>, Integer, NotUsed> flow = Flow.<Pair<String, Integer>>create()
                                    .mapConcat(p ->
                                        new ArrayList<>(Collections.nCopies(p.second(), p.first()))
                                    )
                                    .mapAsync(pair.second(), url -> {
                                        long startTime = System.currentTimeMillis();
                                        asyncHttpClient().prepareGet(url).execute();
                                        long endTime = System.currentTimeMillis();
                                        return CompletableFuture.completedFuture((int)(endTime - startTime));
                                    });
                            return Source.single(pair)
                                    .via(flow)
                                    .toMat(Sink.fold(0, Integer::sum), Keep.right())
                                    .run(materializer)
                                    .thenApply(sum -> new Pair<>(pair.first(), sum / pair.second()));
                        }))
                .map((Pair<String, Integer> p) -> {
                    cache.tell(p, ActorRef.noSender());
                    return HttpResponse.create().withEntity(HttpEntities.create(p.second().toString()));
                });

    }

    public static void main(String[] args) throws IOException {
        ActorSystem system = ActorSystem.create("Akka-lab5");
        ActorRef cache = system.actorOf(Props.create(CacheActor.class));

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        final Flow<HttpRequest, HttpResponse, NotUsed> flow = createFlow(system, cache, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(flow, ConnectHttp.toHost("localhost", 8080), materializer);

        System.out.println("Server start at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();
        binding.thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> {
                    system.terminate();
                    try {
                        asyncHttpClient().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
