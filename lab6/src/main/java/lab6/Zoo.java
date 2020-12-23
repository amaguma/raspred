package lab6;

import akka.actor.ActorRef;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zoo {
    private static final int TIMEOUT = 5000;
    private static final String CONNECTING_STRING = "127.0.0.1:2181";
    private static final String SERVERS = "/servers";
    private static final String SERVERS_ROOT = "/servers/";
    private static final String PATH = "/servers/localhost:";

    private  ZooKeeper zooKeeper;
    private  ActorRef storeActor;
    private int port;

    public Zoo(ActorRef storeActor, int port) {
        try {
            this.zooKeeper = new ZooKeeper(CONNECTING_STRING, TIMEOUT, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.storeActor = storeActor;
        this.port = port;
        sendServers();
        initZoo();
    }

    private void sendServers() {
        try {
            List<String> serversName = zooKeeper.getChildren(SERVERS, watchedEvent -> {
                if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    sendServers();
                }
            });

            List<String> servers = new ArrayList<>();
            for (String s : serversName) {
                byte[] port = this.zooKeeper.getData(SERVERS_ROOT + s, false, null);
                servers.add(new String(port));
            }
            this.storeActor.tell(new ServerMsg(servers), ActorRef.noSender());
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initZoo() {
       try {
           this.zooKeeper.create(PATH + this.port,
                   String.valueOf(this.port).getBytes(),
                   ZooDefs.Ids.OPEN_ACL_UNSAFE,
                   CreateMode.EPHEMERAL
           );
       } catch (KeeperException | InterruptedException e) {
           e.printStackTrace();
       }
    }
}
