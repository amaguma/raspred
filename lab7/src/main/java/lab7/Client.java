package lab7;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        ZContext context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.REQ);
        socket.connect("tcp://localhost:5556");

        Scanner in = new Scanner(System.in);
        while (true) {
            String command = in.nextLine();

            if (command.equals("")) {
                break;
            }

            String[] commands = command.split(" ");

            if (commands.length == 2 && commands[0].equals("GET")) {
                int key = Integer.parseInt(commands[1]);

                String response;
                ZFrame frame = new ZFrame(String.format("GET %d", key));
                
            }
        }
    }
}
