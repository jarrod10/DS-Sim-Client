package Protocol;

public class Job {

    public JobState jobState;

    public int submitTime;
    public int jobID;
    public int estRunTime;
    public int cpu;
    public int memory;
    public int disk;

    /**
     * New job constructor that sets up all the variables of a job to be passed around as an object
     */
    public Job(int submitTime, int jobID, int estRunTime, int cpu, int memory, int disk) {
        this.jobID = jobID;
        this.submitTime = submitTime;
        this.estRunTime = estRunTime;
        this.cpu = cpu;
        this.memory = memory;
        this.disk = disk;
    }

    public Job(String[] messageParts) {
        this.submitTime = Integer.parseInt(messageParts[1]); // jobID
        this.jobID = Integer.parseInt(messageParts[2]); // submitTime
        this.estRunTime = Integer.parseInt(messageParts[3]); // estRuntime
        this.cpu = Integer.parseInt(messageParts[4]); // core
        this.memory = Integer.parseInt(messageParts[5]); // memory
        this.disk = Integer.parseInt(messageParts[6]); // disk
    }

    public String toMessagePartSCHD() {
        return String.valueOf(jobID);
    }
}
