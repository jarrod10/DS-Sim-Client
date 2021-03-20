import java.util.ArrayList;

public class SystemInfomation {
    private static SystemInfomation instance = null;
    int serverCount = 0;
    ArrayList<ArrayList<Object>> servers = new ArrayList<ArrayList<Object>>(serverCount);

    SystemInfomation() {}

    // SystemInfomation(String _type, int _limit, int _bootupTime, Float _hourlyRate, int _coreCount, int _memory, int _disk) {
    //     type = _type;
    //     limit = _limit;
    //     bootupTime = _bootupTime;
    //     hourlyRate = _hourlyRate;
    //     coreCount = _coreCount;
    //     memory = _memory;
    //     disk = _disk;
    // }

    /**
     * Call for each server to be added.
     * Adds server data from XML to a globally accessable server collection
     * @param _type
     * @param _limit
     * @param _bootupTime
     * @param _hourlyRate
     * @param _coreCount
     * @param _memory
     * @param _disk
     */
    public void setSystemInfomation(String _type, int _limit, int _bootupTime, Float _hourlyRate, int _coreCount, int _memory, int _disk) {
        ArrayList<Object> serverData = new ArrayList<Object>(7);
        serverData.add(_type);
        serverData.add(_limit);
        serverData.add(_bootupTime);
        serverData.add(_hourlyRate);
        serverData.add(_coreCount);
        serverData.add(_memory);
        serverData.add(_disk);

        servers.add(serverData);
        serverCount++;
    }

    public static SystemInfomation getInstance() {
        if (instance == null)
            instance = new SystemInfomation();
        return instance;
    }
}