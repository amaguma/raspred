package lab6;

import akka.actor.ActorRef;
import org.apache.zookeeper.*;

import java.util.ArrayList;
import java.util.List;

public class Zoo implements Watcher {
    private ZooKeeper zooKeeper;
    private ActorRef storeActor;
    private int port;

    public Zoo(ActorRef storeActor, int port) throws KeeperException, InterruptedException {
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
        List<String> serversName = zooKeeper.getChildren("/servers", watchedEvent -> {
            if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                sendServers();
            }
        });
        List<String> servers = new ArrayList<>();
        for (String s : serversName) {
            byte[] port = zooKeeper.getData("/servers/" + s, false, null);
        }
        this.storeActor.tell(new ServerMsg(servers), ActorRef.noSender());
    }

    public void initZoo() {
        this.zooKeeper.create("/servers/localhost:" + this.port,
                String.valueOf(this.port).getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL
                );
        this.storeActor.tell();
    }
}
