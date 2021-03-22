import java.util.regex.*;

public class Job {
    int id;
    String type;
    int submissionTime;
    int EstimatedRunTime;
    int cpuCores;
    int memory;
    int disk;

    // (todo) cast all message tokens into variable types
    /**
     * new job constructor
     * @param messageTokens A String array of ...
     */
    Job(String[] messageTokens) {
        id = (Integer) messageTokens[2];
        type = messageTokens[0];
        submissionTime = messageTokens[1];
        EstimatedRunTime = messageTokens[3];
        cpuCores = messageTokens[4];
        memory = messageTokens[5];
        disk = messageTokens[6];
    }
}
