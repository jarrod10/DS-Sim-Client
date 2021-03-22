public class Job {
    int id;
    String type;
    int submissionTime;
    int EstimatedRunTime;
    int cpuCores;
    int memory;
    int disk;

    Job(String rawString) {
        parseJob(rawString);
    }

    private void parseJob(String rawString) {
        
    }
    
}
