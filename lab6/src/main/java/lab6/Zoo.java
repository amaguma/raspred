package lab6;

import akka.actor.ActorRef;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zoo {
    private ZooKeeper zooKeeper;
    private ActorRef storeActor;
    private int port;

    public Zoo(ActorRef storeActor, int port) {
        try {
            this.zooKeeper = new ZooKeeper(
                    "127.0.0.1:2181",
                    5000,
                    null
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.storeActor = storeActor;
        this.port = port;
        sendServers();
        initZoo();
    }

    public void sendServers() {
        try {
            List<String> serversName = zooKeeper.getChildren("/servers", watchedEvent -> {
                if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    sendServers();
                }
            });

            List<String> servers = new ArrayList<>();
            for (String s : serversName) {
                byte[] port = zooKeeper.getData("/servers/" + s, false, null);
            }
            this.storeActor.tell(new ServerMsg(servers), ActorRef.noSender());
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initZoo() {
       try {
           this.zooKeeper.create("/servers/localhost:" + this.port,
                   String.valueOf(this.port).getBytes(),
                   ZooDefs.Ids.OPEN_ACL_UNSAFE,
                   CreateMode.EPHEMERAL
           );
       } catch (KeeperException | InterruptedException e) {
           e.printStackTrace();
       }
    }
}