package lab7;

import org.zeromq.*;

import java.util.ArrayList;

public class Proxy {

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

    public static void setHeartbeat(String id) {
        for (Config config : configs) {
            if (config.getId().equals(id)) {
                config.setTime(System.currentTimeMillis());
                break;
            }
        }
    }

    public static void delConfigs() {
        for (Config config : configs) {
            if (config.isNotAlive()) {
                configs.remove(config);
                break;
            }
        }
    }

    public static void main(String[] args) {
        ZContext context = new ZContext();
        frontend = context.createSocket(SocketType.ROUTER);
        frontend.bind(Tools.FRONTEND_SOCKET_ADDRESS);
        backend = context.createSocket(SocketType.ROUTER);
        backend.bind(Tools.BACKEND_SOCKET_ADDRESS);

        configs = new ArrayList<>();
        System.out.println(Tools.PROXY_MSG);

        ZMQ.Poller items = context.createPoller(2);
        items.register(frontend, ZMQ.Poller.POLLIN);
        items.register(backend, ZMQ.Poller.POLLIN);

        while (!Thread.currentThread().isInterrupted()) {
            if (items.poll(Tools.TIMEOUT) == -1) {
                break;
            }

            if (items.pollin(0)) {
                ZMsg msg = ZMsg.recvMsg(frontend);
                ZFrame frame = msg.getLast();

                String command = new String(frame.getData(), ZMQ.CHARSET);
                String[] commands = Tools.splitStr(command);

                if (commands.length == Tools.GET_REQ_LENGTH && commands[Tools.INDEX_REQ].equals(Tools.GET_REQ)) {
                    int key = Integer.parseInt(commands[Tools.INDEX_KEY]);
                    boolean get = sendGetMsg(key, msg);
                    if (!get) {
                        msg.getLast().reset(Tools.WRONG_KEY);
                        msg.send(frontend);
                    }
                }
                if (commands.length == Tools.PUT_REQ_LENGTH && commands[Tools.INDEX_REQ].equals(Tools.PUT_REQ)) {
                    int key = Integer.parseInt(commands[Tools.INDEX_KEY]);

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
                    String[] heartbeatArg = Tools.splitStr(heartbeat);

                    if (heartbeatArg.length == Tools.INIT_LENGTH && heartbeatArg[Tools.INDEX_REQ].equals("INIT")) {
                        int min = Integer.parseInt(heartbeatArg[Tools.MIN_THRESHOLD_INDEX]);
                        int max = Integer.parseInt(heartbeatArg[Tools.MAX_THRESHOLD_INDEX]);
                        configs.add(new Config(
                           idFrame,
                           id,
                           System.currentTimeMillis(),
                           min,
                           max
                        ));
                    } else if (heartbeatArg.length == Tools.HEARTBEAT_LENGTH && heartbeatArg[Tools.INDEX_REQ].equals("HB")) {
                        setHeartbeat(id);
                    }
                } else {
                    msg.send(frontend);
                }
            }
            delConfigs();
        }
        context.destroySocket(frontend);
        context.destroySocket(backend);
        context.destroy();
    }
}
