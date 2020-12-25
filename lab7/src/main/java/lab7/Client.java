package lab7;

import org.zeromq.*;

import java.util.Scanner;

public class Client {


    public static void main(String[] args) {
        ZContext context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.REQ);
        socket.connect(Tools.FRONTEND_SOCKET_ADDRESS);
        System.out.println(Tools.CLIENT_MSG);
        Scanner in = new Scanner(System.in);
        while (true) {
            String command = in.nextLine();

            if (command.equals(Tools.EMPTY_COMMAND)) {
                System.out.println("error");
                break;
            }

            String[] commands = Tools.splitStr(command);

            if (commands.length == Tools.GET_REQ_LENGTH && commands[Tools.INDEX_REQ].equals(Tools.GET_REQ)) {
                int key = Integer.parseInt(commands[Tools.INDEX_KEY]);

                String str = String.format(Tools.GET_REQ + " %d", key);
                String response = sendAndReceive(socket, str);
                System.out.println(response);
            } else if (commands.length == Tools.PUT_REQ_LENGTH && commands[Tools.INDEX_REQ].equals(Tools.PUT_REQ)) {
                int key = Integer.parseInt(commands[Tools.INDEX_KEY]);
                int value = Integer.parseInt(commands[Tools.INDEX_VALUE]);

                String str = String.format(Tools.PUT_REQ + " %d %d", key, value);
                String response = sendAndReceive(socket, str);
                System.out.println(response);
            } else {
                System.out.println(Tools.WRONG_COMMAND);
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
            return Tools.EMPTY_MSG;
        } else {
            return new String(msg.getFirst().getData(), ZMQ.CHARSET);
        }
    }
}
