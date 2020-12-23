package lab6;

import akka.actor.ActorRef;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Server implements Watcher {
    private ZooKeeper zooKeeper;
    private ActorRef storeActor;
    private int port;

    public Server(ActorRef storeActor) 
}
