package lab5;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.HashMap;
import java.util.Map;

public class CacheActor extends AbstractActor {
    private final Map<String, Integer> cache = new HashMap<>();

    @Override
    public Receive createReceive() {
        
    }
}
