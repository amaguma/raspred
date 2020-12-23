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
        this.storeActor = storeActor;
        this.port = port;
        this.zooKeeper = new ZooKeeper(
                "127.0.0.1:2181",
                5000,
                null
        );
        sendServers();
    }

    public void sendServers() throws KeeperException, InterruptedException {
        List<String> servers = zooKeeper.getChildren("/servers", this);
        this.storeActor.tell(new ServerMsg(servers), ActorRef.noSender());
    }
}
