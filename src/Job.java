public class Job {
    int id;
    String type;
    int submissionTime;
    int EstimatedRunTime;
    int cpuCores;
    int memory;
    int disk;

    /**
     * New job constructor that sets up all the variables of a job to be passed around as an object
     * @param messageTokens A String array of all the tokenised message data
     */
    Job(String[] messageTokens) {
        id = Integer.parseInt(messageTokens[2]);
        type = messageTokens[0];
        submissionTime = Integer.parseInt(messageTokens[1]);
        EstimatedRunTime = Integer.parseInt(messageTokens[3]);
        cpuCores = Integer.parseInt(messageTokens[4]);
        memory = Integer.parseInt(messageTokens[5]);
        disk = Integer.parseInt(messageTokens[6]);
    }

}
