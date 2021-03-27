package Protocol;

public class Server {

    public String serverType;
    public int serverID;
    public ServerState serverState;
    public int curStartTime;

    public int core;
    public int mem;
    public int disk;

    public int limit;
    public int bootupTime;
    public float hourlyRate;

    public Server() {
    }

    // Constructor to create server object from GETS command
    public Server(String serverType, int serverID, ServerState serverState, int curStartTime, int core, int mem, int disk) {
        this.serverType = serverType;
        this.serverID = serverID;
        this.serverState = serverState;
        this.curStartTime = curStartTime;
        this.core = core;
        this.mem = mem;
        this.disk = disk;
    }

    // Constructor to create server from XML
    public Server(String serverType, int limit, int bootupTime, float hourlyRate, int core, int mem, int disk) {
        this.serverType = serverType;
        this.limit = limit;
        this.bootupTime = bootupTime;
        this.hourlyRate = hourlyRate;
        this.core = core;
        this.mem = mem;
        this.disk = disk;
    }

    public String toMessagePartSCHD() {
        return String.valueOf(serverType) + String.valueOf(serverID);
    }

}
