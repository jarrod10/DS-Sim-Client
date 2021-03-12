package Protocol;

public interface Handler {

    public Action enterState();

    public Action handleMessage(String message) throws UnrecognisedCommandException;

}
