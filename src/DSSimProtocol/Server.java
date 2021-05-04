package DSSimProtocol;

public class Server {

    /**
     * Represents a server that can execute jobs in the simulation. This class does not represent the DSSimServer
     * that runs the simulation.
     */

    public String serverType;

    public enum ServerState {
        INACTIVE(0),
        BOOTING(1),
        IDLE(2),
        ACTIVE(3),
        UNAVAILABLE(4);

        private final int index;

        ServerState(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
    
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

    /**
     * This constructor is best suited for creating server objects from the response to a GETS command
     *
     * @param serverType   The name of the server type
     * @param serverID     An arbitrary but unique ID for each server
     * @param serverState  The state of the server, either INACTIVE, BOOTING, IDLE, ACTIVE, or UNAVAILABLE
     * @param curStartTime The start time of the currently running task
     * @param core         The available CPU core
     * @param mem          The available memory
     * @param disk         The available disk space
     */
    public Server(String serverType, int serverID, ServerState serverState, int curStartTime, int core, int mem, int disk) {
        this.serverType = serverType;
        this.serverID = serverID;
        this.serverState = serverState;
        this.curStartTime = curStartTime;
        this.core = core;
        this.mem = mem;
        this.disk = disk;
    }

    /**
     * This constructor is best suited for creating server objects from the ds-system.xml file
     *
     * @param serverType The name of the server type
     * @param limit      The number of servers of a particular type
     * @param bootupTime The time taken to bootup a server of this type
     * @param hourlyRate The cost per hour for renting a server of this type
     * @param core       The number of CPU cores
     * @param mem        The amount of memory in MB
     * @param disk       The amount of disk space in MB
     */
    public Server(String serverType, int limit, int bootupTime, float hourlyRate, int core, int mem, int disk) {
        this.serverType = serverType;
        this.limit = limit;
        this.bootupTime = bootupTime;
        this.hourlyRate = hourlyRate;
        this.core = core;
        this.mem = mem;
        this.disk = disk;
    }
}
