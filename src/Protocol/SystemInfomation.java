package Protocol;

import java.util.ArrayList;

public class SystemInfomation {

    public static boolean verbose = false;
    public static boolean debug = false;
    public static String configurationPath = "../../ds-system.xml";
    public static String algorithmName = "allToLargest";
    public static String remoteAddress = "127.0.0.1";
    public static int port = 50000;

    public static ArrayList<Server> serverList = new ArrayList<>();

    private SystemInfomation() {}

    private static SystemInfomation instance = null;
    public static SystemInfomation getInstance() {
        if (instance == null)
            instance = new SystemInfomation();
        return instance;
    }

    /**
     * Call for each server to be added.
     * Adds server data from XML to a globally accessible server collection
     */
    public void addServer(String serverType, int limit, int bootupTime, float hourlyRate, int core, int mem, int disk) {
        Server newServer = new Server(serverType, limit, bootupTime, hourlyRate, core, mem, disk);
        serverList.add(newServer);
    }

    /**
     * Calculates highest core count server.
     * @param serverList List of all servers from XML file
     * @return Returns Server object with highest Core count.
     */
        public static Server mostCores() {
            int highestId = 0;
            if (serverList.size() > 0) {
                for (int i = 0; i < serverList.size(); i++) {
                    if (serverList.get(i).core > serverList.get(highestId).core)
                        highestId = serverList.get(i).serverID;
                }
                return serverList.get(highestId);
            }
            return null;
        }

}