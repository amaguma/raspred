package lab7;

import org.zeromq.*;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    public static void main(String[] args) {

        private static final String SOCKET_ADDRESS = "tcp://localhost:5555";

        int min = Integer.parseInt(args[0]);
        int max = Integer.parseInt(args[1]);

        ZContext context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.DEALER);
        socket.connect("tcp://localhost:5555");
        ZFrame init = new ZFrame(String.format("INIT %d %d", min, max));
        init.send(socket, 0);
        System.out.println("Storage");

        ZMQ.Poller poller = context.createPoller(1);
        poller.register(socket, ZMQ.Poller.POLLIN);

        Map<Integer, Integer> storage = new HashMap<>();

        long time = System.currentTimeMillis() + 2000;

        while (!Thread.currentThread().isInterrupted()) {
            if (poller.poll(2000) == -1) {
                break;
            }
            if (poller.pollin(0)) {
                ZMsg msg = ZMsg.recvMsg(socket);
                if (msg == null) {
                    break;
                }
                if (msg.size() == 3) {
                    ZFrame frame = msg.getLast();
                    String command = new String(frame.getData(), ZMQ.CHARSET);

                    String[] commands = command.split(" ");

                    for (String s : commands) {
                        System.out.println(s);
                    }

                    if (commands.length == 2 && commands[0].equals("GET")) {
                        int key = Integer.parseInt(commands[1]);
                        System.out.println("GET " + key);

                        String response = "Wrong key";

                        if (storage.containsKey(key)) {
                            int value = storage.get(key);
                            response = Integer.toString(value);
                        }
                        msg.getLast().reset(response);
                        msg.send(socket);
                    }
                    if (commands.length == 3 && commands[0].equals("SET")) {
                        int key = Integer.parseInt(commands[1]);
                        int value = Integer.parseInt(commands[2]);
                        System.out.println("SET " + key + " " + value);
                        storage.put(key, value);
                        msg.destroy();
                    }
                }
            }
            if (System.currentTimeMillis() >= time) {
                System.out.println("RELOAD");
                time = System.currentTimeMillis() + 2000;
            }
        }
        context.destroySocket(socket);
        context.destroy();
    }
}
