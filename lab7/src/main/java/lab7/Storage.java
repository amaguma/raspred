package lab7;

import org.zeromq.*;

import java.util.HashMap;
import java.util.Map;

public class Storage {

    private static final String SOCKET_ADDRESS = "tcp://localhost:5555";
    private static final int TIMEOUT = 2000;
    private static final String GET = "GET";
    private static final String SET = "SET";
    private static final int INDEX_REQ = 0;
    private static final int INDEX_KEY = 1;
    private static final int INDEX_VALUE = 2;
    private static final int GET_REQ_LENGTH = 2;
    private static final int SET_REQ_LENGTH = 3;
    

    public static void main(String[] args) {

        int min = Integer.parseInt(args[0]);
        int max = Integer.parseInt(args[1]);

        ZContext context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.DEALER);
        socket.connect(SOCKET_ADDRESS);
        ZFrame init = new ZFrame(String.format("INIT %d %d", min, max));
        init.send(socket, 0);
        System.out.println("Storage");

        ZMQ.Poller poller = context.createPoller(1);
        poller.register(socket, ZMQ.Poller.POLLIN);

        Map<Integer, Integer> storage = new HashMap<>();

        long time = System.currentTimeMillis() + TIMEOUT;

        while (!Thread.currentThread().isInterrupted()) {
            if (poller.poll(TIMEOUT) == -1) {
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

                    if (commands.length == GET_REQ_LENGTH && commands[INDEX_REQ].equals(GET)) {
                        int key = Integer.parseInt(commands[INDEX_KEY]);
                        System.out.println(GET + " " + key);

                        String response = "Wrong key";

                        if (storage.containsKey(key)) {
                            int value = storage.get(key);
                            response = Integer.toString(value);
                        }
                        msg.getLast().reset(response);
                        msg.send(socket);
                    }
                    if (commands.length == SET_REQ_LENGTH && commands[INDEX_REQ].equals(SET)) {
                        int key = Integer.parseInt(commands[INDEX_KEY]);
                        int value = Integer.parseInt(commands[INDEX_VALUE]);
                        System.out.println(SET + " " + key + " " + value);
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
