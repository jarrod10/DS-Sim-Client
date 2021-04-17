package DSSimProtocol;

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
