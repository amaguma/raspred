package lab7;

import org.zeromq.*;

import java.util.HashMap;
import java.util.Map;

public class Storage {


    public static void main(String[] args) {

        int min = Integer.parseInt(args[Tools.INDEX_MIN]);
        int max = Integer.parseInt(args[Tools.INDEX_MAX]);

        ZContext context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.DEALER);
        socket.connect(Tools.BACKEND_SOCKET_ADDRESS);
        ZFrame init = new ZFrame(String.format("INIT %d %d", min, max));
        init.send(socket, 0);
        System.out.println("Storage");

        ZMQ.Poller poller = context.createPoller(1);
        poller.register(socket, ZMQ.Poller.POLLIN);

        Map<Integer, Integer> storage = new HashMap<>();

        long time = System.currentTimeMillis() + Tools.TIMEOUT;

        while (!Thread.currentThread().isInterrupted()) {
            if (poller.poll(Tools.TIMEOUT) == -1) {
                break;
            }
            if (poller.pollin(0)) {
                ZMsg msg = ZMsg.recvMsg(socket);
                if (msg == null) {
                    break;
                }
                if (msg.size() == Tools.MESSAGE_SIZE) {
                    ZFrame frame = msg.getLast();
                    String command = new String(frame.getData(), ZMQ.CHARSET);

                    String[] commands = command.split(Tools.DELIMITER);

                    for (String s : commands) {
                        System.out.println(s);
                    }

                    if (commands.length == Tools.GET_REQ_LENGTH && commands[Tools.INDEX_REQ].equals(Tools.GET_REQ)) {
                        int key = Integer.parseInt(commands[Tools.INDEX_KEY]);
                        System.out.println(GET_REQ + " " + key);

                        String response = "Wrong key";

                        if (storage.containsKey(key)) {
                            int value = storage.get(key);
                            response = Integer.toString(value);
                        }
                        msg.getLast().reset(response);
                        msg.send(socket);
                    }
                    if (commands.length == PUT_REQ_LENGTH && commands[INDEX_REQ].equals(PUT_REQ)) {
                        int key = Integer.parseInt(commands[INDEX_KEY]);
                        int value = Integer.parseInt(commands[INDEX_VALUE]);
                        System.out.println(PUT_REQ + " " + key + " " + value);
                        storage.put(key, value);
                        msg.destroy();
                    }
                }
            }
            if (System.currentTimeMillis() >= time) {
                System.out.println("RELOAD");
                time = System.currentTimeMillis() + TIMEOUT;
            }
        }
        context.destroySocket(socket);
        context.destroy();
    }
}
