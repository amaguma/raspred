package lab7;

import org.zeromq.*;

import java.util.ArrayList;

public class Proxy {
    private static ArrayList<Config> configs;
    private static ZMQ.Socket frontend;
    private static ZMQ.Socket backend;

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
                String[] commands = command.split(" ")
            }
        }
    }
}
