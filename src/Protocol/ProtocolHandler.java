package Protocol;

public interface ProtocolHandler {

    public Action onEnterState();

    public Action onReceiveMessage(String message) throws UnrecognisedCommandException;

}
