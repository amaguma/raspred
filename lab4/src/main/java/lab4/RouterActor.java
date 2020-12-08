package lab4;

import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.RoundRobinPool;

import java.time.Duration;
import java.util.Collections;

public class RouterActor extends AbstractActor {
    private ActorRef storageActor;
    private SupervisorStrategy strategy;
    private ActorRef testerActor;

    RouterActor(ActorSystem system) {
        this.storageActor = system.actorOf(Props.create(StorageActor.class), "StorageActor");
        this.strategy = new OneForOneStrategy(5, Duration.ofMinutes(1), Collections.singletonList(Exception.class));
        this.testerActor = system.actorOf(new RoundRobinPool(5).withSupervisorStrategy(strategy).props(Props.create(TesterActor.class, storageActor)));
    }

    private void runTests(TestPackage testPackage) {
        for (TestData test : testPackage.getTests()) {
            test.setParentPackage(testPackage);
            testerActor.tell(test, ActorRef.noSender());
        }
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder
                .create()
                .match(TestPackage.class, msg -> runTests(msg))
                .match(String.class, msg -> storageActor.forward(msg, getContext()))
                .build();
    }
}
