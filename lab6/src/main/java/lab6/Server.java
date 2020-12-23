package lab6;

import akka.actor.ActorRef;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

public class Server implements Watcher {
    private ZooKeeper zooKeeper;
    private ActorRef storeActor;
    private int port;

    public Server(ActorRef storeActor, int port) throws KeeperException, InterruptedException {
        List<String> servers = 
    }
}
