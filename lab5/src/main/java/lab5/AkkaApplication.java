package lab5;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.javadsl.Flow;


public class AkkaApplication {

    public static Flow<HttpRequest, HttpResponse, NotUsed> createFlow(ActorSystem system, ActorRef cache, )
}
