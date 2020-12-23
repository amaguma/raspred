package lab6;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.http.javadsl.model.Uri;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.japi.Pair;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

public class App extends AllDirectives {
    private static Http http;
    private ActorRef storeActor;
    private int port;


    private static CompletionStage<HttpResponse> fetch(String url) {
        return http.singleRequest(HttpRequest.create(url));
    }

    private String createUrl(String serverUrl, String url, int count) {
        return Uri.create(serverUrl).query(Query.create(new Pair[] {
                Pair.create("url", url),
                Pair.create("count", String.valueOf(count - 1))
        })).toString();
    }

    public Route createRoute() {
        return route(get(() ->
                    parameter("url", url ->
                            parameter("count", count -> {
                                if (Integer.parseInt(count) == 0) {
                                    return completeWithFuture(fetch(url));
                                } else {
                                    return completeWithFuture(Patterns.ask(this.storeActor, new RandomServerMsg(), Duration.ofSeconds(5))
                                        .thenApply(serverUrl -> (String)serverUrl)
                                            .thenCompose((serverUrl) -> fetch(createUrl(serverUrl, url, Integer.parseInt(count))))
                                    );
                                }
                            }))
                ));
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        final ActorSystem system = ActorSystem.create("routes");
        ActorRef storageActor = system.actorOf(Props.create(StorageActor.class));
        final Http http = Http.get(system);
        Zoo zoo = new Zoo(storageActor, 8080);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        final App server = new App();
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = server.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost("localhost", 8080),
                materializer
        );
        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());
    }
}
