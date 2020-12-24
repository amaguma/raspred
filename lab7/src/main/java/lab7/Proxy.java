package lab7;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

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
    }
}
