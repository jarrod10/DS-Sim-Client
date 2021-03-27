package Protocol;

public class Action {

    public Intent intent;

    // For Intent.SWITCH_STATE
    public ProtocolState state;

    // For Intent.SEND_MESSAGE
    public String message;

    // For Intent.COMMAND_SCHD
    public Job job;
    public Server server;

    // For Intent.COMMAND_CNTJ
    public JobState jobState;

    public Action() {
    }

    public Action(Intent intent) {
        this.intent = intent;
    }

    // An action to send arbitrary text to DS-Sim-Server
    public Action(Intent intent, String message) {
        this.intent = intent;
        this.message = message;
    }

    // An action to schedule a job
    public Action(Intent intent, Job job, Server server) {
        this.intent = intent;
        this.job = job;
        this.server = server;
    }
    public Action(Intent intent, Server server, Job job) {
        this.intent = intent;
        this.job = job;
        this.server = server;
    }

    public Action(Intent intent, Job job, Server server, JobState jobState) {
        this.intent = intent;
        this.job = job;
        this.server = server;
        this.jobState = jobState;
    }

    // An action to switch protocol states in the client
    public Action(Intent intent, ProtocolState state) {
        this.intent = intent;
        this.state = state;
    }

}
