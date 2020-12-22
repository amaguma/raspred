package lab6;

import akka.actor.AbstractActor;

import java.util.List;
import java.util.Random;

public class StorageActor extends AbstractActor {
    private List<String> storage;
    private final Random random;
}
