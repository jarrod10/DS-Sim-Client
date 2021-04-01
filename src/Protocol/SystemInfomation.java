package Protocol;

import java.util.ArrayList;

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

    public static Server mostCores(ArrayList<Server> serverList) {
        int highestId = 0;
//        Server server = new Server();
        if (serverList.size() > 0) {
            for (int i = 0; i < serverList.size(); i++) {
                if (serverList.get(i).core > serverList.get(highestId).core) {
                    highestId = serverList.get(i).serverID;
                }
//                highestId = serverList.get(i).;
            }
            return serverList.get(highestId);
        }

        return null;
    }

}