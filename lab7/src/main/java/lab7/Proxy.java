package lab7;

import org.zeromq.*;

import java.util.ArrayList;

public class Proxy {

    private static final String FRONTEND_SOCKET_ADDRESS = "tcp://localhost:5556";
    private static final String BACKEND_SOCKET_ADDRESS = "tcp://localhost:5555";
    private static final int TIMEOUT = 2000;
    private static final String DELIMITER = " ";
    private static final String GET_REQ = "GET";
    private static final String SET_REQ = "SET";
    private static final int INDEX_REQ = 0;
    private static final int INDEX_KEY = 1;
    private static final int INDEX_MIN = 1;
    private static final int INDEX_MAX = 2;
    private static final int GET_REQ_LENGTH = 2;
    private static final int SET_REQ_LENGTH = 3;

    private static ArrayList<Config> configs;
    private static ZMQ.Socket frontend;
    private static ZMQ.Socket backend;

    public static boolean sendGetMsg(int key, ZMsg msg) {
        for (Config config : configs) {
            if (config.getMin() <= key && key <= config.getMax()) {
                config.getAddress().send(backend, ZFrame.REUSE + ZFrame.MORE);
                msg.send(backend, false);
                return true;
            }
        }
        return false;
    }

    public static int sendSetMsg(int key, ZMsg msg) {
        int count = 0;
        for (Config config : configs) {
            if (config.getMin() <= key && key <= config.getMax()) {
                config.getAddress().send(backend, ZFrame.REUSE + ZFrame.MORE);
                msg.send(backend, false);
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        ZContext context = new ZContext();
        frontend = context.createSocket(SocketType.ROUTER);
        frontend.bind(FRONTEND_SOCKET_ADDRESS);
        backend = context.createSocket(SocketType.ROUTER);
        backend.bind(BACKEND_SOCKET_ADDRESS);

        configs = new ArrayList<>();
        System.out.println("Proxy");

        ZMQ.Poller items = context.createPoller(2);
        items.register(frontend, ZMQ.Poller.POLLIN);
        items.register(backend, ZMQ.Poller.POLLIN);

        while (!Thread.currentThread().isInterrupted()) {
            if (items.poll(TIMEOUT) == -1) {
                break;
            }

            if (items.pollin(0)) {
                ZMsg msg = ZMsg.recvMsg(frontend);
                ZFrame frame = msg.getLast();

                String command = new String(frame.getData(), ZMQ.CHARSET);
                String[] commands = command.split(DELIMITER);

                if (commands.length == GET_REQ_LENGTH && commands[INDEX_REQ].equals(GET_REQ)) {
                    int key = Integer.parseInt(commands[INDEX_KEY]);
                    boolean get = sendGetMsg(key, msg);
                    if (!get) {
                        msg.getLast().reset("Wrong key");
                        msg.send(frontend);
                    }
                }
                if (commands.length == SET_REQ_LENGTH && commands[INDEX_REQ].equals(SET_REQ)) {
                    int key = Integer.parseInt(commands[INDEX_KEY]);

                    int count = sendSetMsg(key, msg);
                    String response;

                    if (count > 0) {
                        response = "Set in " + count + " storages";
                    } else {
                        response = "No storages responsible for key";
                    }
                    ZMsg respMsg = new ZMsg();
                    respMsg.add(new ZFrame(response));
                    respMsg.wrap(msg.getFirst());
                    respMsg.send(frontend);
                }
            } else if (items.pollin(1)) {
                ZMsg msg = ZMsg.recvMsg(backend);

                ZFrame idFrame = msg.unwrap();
                String id = new String(idFrame.getData(), ZMQ.CHARSET);
                if (msg.size() == 1) {
                    ZFrame frame = msg.getFirst();
                    String heartbeat = new String(frame.getData(), ZMQ.CHARSET);
                    String[] heartbeatArg = heartbeat.split(DELIMITER);

                    if (heartbeatArg.length == 3 && heartbeatArg[INDEX_REQ].equals("INIT")) {
                        int min = Integer.parseInt(heartbeatArg[INDEX_MIN]);
                        int max = Integer.parseInt(heartbeatArg[INDEX_MAX]);
                        configs.add(new Config(
                           idFrame,
                           id,
                           System.currentTimeMillis(),
                           min,
                           max
                        ));
                    } else if (heartbeatArg.length == 1 && heartbeatArg[INDEX_REQ].equals("HB")) {
                        setHeartbeat(id);
                    }
                } else {
                    msg.send(frontend);
                }
            }

            for (Config config : configs) {
                if (config.isNotAlive()) {
                    configs.remove(config);
                    break;
                }
            }
        }
        context.destroySocket(frontend);
        context.destroySocket(backend);
        context.destroy();
    }

    public static void setHeartbeat(String id) {
        for (Config config : configs) {
            if (config.getId().equals(id)) {
                config.setTime(System.currentTimeMillis());
                break;
            }
        }
    }
}
