package lab7;

import org.zeromq.*;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    public static void main(String[] args) {
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
                    ZFrame frame = msg.getFirst();
                    String command = new String(frame.getData(), ZMQ.CHARSET);
                }
            }
        }
    }
}
