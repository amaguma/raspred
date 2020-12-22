package lab6;

import java.io.Serializable;
import java.util.List;

public class ServerMsg {
    private List<String> servers;

    public ServerMsg(List<String> servers) {
        this.servers = servers;
    }

    public List<String> getServers() {
        return servers;
    }
}
