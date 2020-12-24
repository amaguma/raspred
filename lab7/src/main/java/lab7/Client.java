package lab7;

import org.zeromq.*;

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

                ZFrame frame = new ZFrame(String.format("GET %d", key));
                ZMsg msg = ZMsg.recvMsg(socket);
                String response;
                if (msg == null) {
                    response = "Empty message";
                } else {
                    response = new String(msg.getFirst().getData(), ZMQ.CHARSET);
                }
                System.out.println(response);
            }
        }
    }
}
