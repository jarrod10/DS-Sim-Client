package Protocol;

import java.util.ArrayList;
import java.util.HashSet;

public class SystemInfomation {

    public ArrayList<Server> serverList = new ArrayList<>();

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

}