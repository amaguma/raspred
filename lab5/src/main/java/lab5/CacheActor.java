package lab5;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;


import java.util.HashMap;
import java.util.Map;

public class CacheActor extends AbstractActor {
    private final Map<String, Integer> cache = new HashMap<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message ->
                        getSender().tell(cache.getOrDefault(message, -1), ActorRef.noSender()))
                .match(UrlTest.class, message ->
                        cache.putIfAbsent(message.getUrl(), message.getTime()))
                .build();
    }
}
