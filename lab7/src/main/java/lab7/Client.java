package lab7;

import org.zeromq.*;

import java.util.Scanner;

public class Client {

    private static final String SOCKET_ADDRESS = "tcp://localhost:5556";
    private static final String DELIMITER = " ";
    private static final String EMPTY_COMMAND = "";
    private static final String GET_REQ = "GET";
    private static final String SET_REQ = "SET";
    private static final int GET_REQ_LENGTH = 2;
    private static final int SET_REQ_LENGTH = 3;

    public static void main(String[] args) {
        ZContext context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.REQ);
        socket.connect(SOCKET_ADDRESS);

        Scanner in = new Scanner(System.in);
        while (true) {
            String command = in.nextLine();

            if (command.equals(EMPTY_COMMAND)) {
                break;
            }

            String[] commands = command.split(DELIMITER);

            if (commands.length == GET_REQ_LENGTH && commands[0].equals(GET_REQ)) {
                int key = Integer.parseInt(commands[1]);

                String str = String.format(GET_REQ + " %d", key);
                String response = sendAndReceive(socket, str);
                System.out.println(response);
            } else if (commands.length == SET_REQ_LENGTH && commands[0].equals(SET_REQ)) {
                int key = Integer.parseInt(commands[1]);
                int value = Integer.parseInt(commands[2]);

                String str = String.format(SET_REQ + " %d %d", key, value);
                String response = sendAndReceive(socket, str);
                System.out.println(response);
            } else {
                System.out.println("Unexpected command");
            }
        }
        context.destroySocket(socket);
        context.destroy();
    }

    public static String sendAndReceive(ZMQ.Socket socket, String str) {
        ZFrame frame = new ZFrame(str);
        frame.send(socket, 0);
        ZMsg msg = ZMsg.recvMsg(socket);
        if (msg == null) {
            return "Empty message";
        } else {
            return new String(msg.getFirst().getData(), ZMQ.CHARSET);
        }
    }
}
