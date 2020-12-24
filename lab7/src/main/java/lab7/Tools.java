package lab7;

public class Tools {

    private static final int TIMEOUT = 2000;
    private static final String GET_REQ = "GET";
    private static final String PUT_REQ = "PUT";
    private static final int INDEX_REQ = 0;
    private static final int INDEX_KEY = 1;
    private static final int INDEX_VALUE = 2;
    private static final int GET_REQ_LENGTH = 2;
    private static final int PUT_REQ_LENGTH = 3;
    private static final int INDEX_MIN = 0;
    private static final int INDEX_MAX = 1;
    private static final int MESSAGE_SIZE = 3;
    private static final String DELIMITER = " ";

    private static final String EMPTY_COMMAND = "";
    private static final String WRONG_COMMAND = "Wrong command";
    private static final String EMPTY_MSG = "Empty message";

    private static final String FRONTEND_SOCKET_ADDRESS = "tcp://localhost:5556";
    private static final String BACKEND_SOCKET_ADDRESS = "tcp://localhost:5555";
    private static final int INIT_LENGTH = 3;
    private static final int HEARTBEAT_LENGTH = 1;

}
