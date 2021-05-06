package DSSimProtocol;

import java.util.ArrayList;

public class SystemInformation {

    public static boolean verbose = false;
    public static boolean debug = false;
    public static String configurationPath = "ds-system.xml";
    // public static String algorithmName = "allToLargest";
    public static Algorithms algorithmName = Algorithms.Default;
    public static String remoteAddress = "127.0.0.1";
    public static int port = 50000;

    public static ArrayList<Server> serverList = new ArrayList<>();

    private SystemInformation() {
    }

    public enum Algorithms {
        Default(0), // AllToLargest
        MinTSC(1);  // Minimum total server cost

        private final int index;

        Algorithms(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public enum JobState {
        SUBMITTED(0),
        WAITING(1),
        RUNNING(2),
        SUSPENDED(3),
        COMPLETED(4),
        PRE_EMPTED(5),
        FAILED(6),
        KILLED(7);

        private final int index;

        JobState(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    /**
     * Adds a server to the internal model of the DSSim system
     *
     * @param serverType The name of the server type
     * @param limit      The number of servers of a particular type
     * @param bootupTime The time taken to bootup a server of this type
     * @param hourlyRate The cost per hour for renting a server of this type
     * @param core       The number of CPU cores
     * @param mem        The amount of memory in MB
     * @param disk       The amount of disk space in MB
     */
    public static void addServer(String serverType, int limit, int bootupTime, float hourlyRate, int core, int mem, int disk) {
        Server newServer = new Server(serverType, limit, bootupTime, hourlyRate, core, mem, disk);
        serverList.add(newServer);
    }

}