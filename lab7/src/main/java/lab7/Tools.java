package lab7;

public class Tools {

    public static final int TIMEOUT = 2000;
    public static final String GET_REQ = "GET";
    public static final String PUT_REQ = "PUT";
    public static final int INDEX_REQ = 0;
    public static final int INDEX_KEY = 1;
    public static final int INDEX_VALUE = 2;
    public static final int GET_REQ_LENGTH = 2;
    public static final int PUT_REQ_LENGTH = 3;
    public static final int INDEX_MIN = 0;
    public static final int INDEX_MAX = 1;
    public static final int MESSAGE_SIZE = 3;
    public static final String DELIMITER = " ";

    public static final String EMPTY_COMMAND = "";
    public static final String WRONG_COMMAND = "Wrong command";
    public static final String EMPTY_MSG = "Empty message";

    public static final String FRONTEND_SOCKET_ADDRESS = "tcp://localhost:5556";
    public static final String BACKEND_SOCKET_ADDRESS = "tcp://localhost:5555";
    public static final int INIT_LENGTH = 3;
    public static final int HEARTBEAT_LENGTH = 1;
    public static final int MIN_THRESHOLD_INDEX = 1;
    public static final int MAX_THRESHOLD_INDEX = 2;

    public static final String CLIENT_MSG = "Client";
    public static final String PROXY_MSG = "Proxy";
    public static final String STORAGE_MSG = "Storage";
    public static final String WRONG_KEY = "Wrong_key";
    public static final String ERROR = "error";
    public static final String NOTIFY = "Init";

    public static String[] splitStr(String command) {
        return command.split(DELIMITER);
    }
}
