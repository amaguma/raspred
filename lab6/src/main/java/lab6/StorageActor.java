package lab6;

import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StorageActor extends AbstractActor {
    private List<String> storage;
    private final Random random;

    public StorageActor() {
        this.storage = new ArrayList<>();
        this.random = new Random();
    }

    public static Props props() {
        return Props.create(StorageActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ServerMsg.class, serverMsg -> this.storage = serverMsg.getServers())
                .match(String.class, msg -> sender().tell(storage.get(random.nextInt(storage.size()), self())))
                .build()
    }
}
