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

    public static void main(String[] args) {
        ZContext context = new ZContext();
        frontend = context.createSocket(SocketType.ROUTER);
        frontend.bind("tcp://localhost:5556");
        backend = context.createSocket(SocketType.ROUTER);
        backend.bind("tcp://localhost:5555");

        configs = new ArrayList<>();
        System.out.println("Proxy");

        ZMQ.Poller items = context.createPoller(2);
        items.register(frontend, ZMQ.Poller.POLLIN);
        items.register(backend, ZMQ.Poller.POLLIN);

        while (!Thread.currentThread().isInterrupted()) {
            if (items.poll(2000) == -1) {
                break;
            }

            if (items.pollin(0)) {
                ZMsg msg = ZMsg.recvMsg(frontend);
                ZFrame frame = msg.getLast();

                String command = new String(frame.getData(), ZMQ.CHARSET);
                String[] commands = command.split(" ");

                if (commands.length == 2 && commands[0].equals("GET")) {
                    int key = Integer.parseInt(commands[1]);
                    boolean get = sendGetMsg(key, msg);
                    if (!get) {
                        msg.getLast().reset("Wrong key");
                        msg.send(frontend);
                    }
                }
                if (commands.length == 3 && commands[0].equals("SET")) {
                    int key = Integer.parseInt(commands[1]);

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
                    String[] heatbeatArg = heartbeat.split(" ");

                    if (heatbeatArg.length == 3 && heatbeatArg[0].equals("INIT")) {
                        int min = Integer.parseInt(heatbeatArg[1]);
                        int max = Integer.parseInt(heatbeatArg[2]);
                        
                    }
                }

            }
        }
    }
}
