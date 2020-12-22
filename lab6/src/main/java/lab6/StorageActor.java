package lab6;

import akka.actor.AbstractActor;

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
}
