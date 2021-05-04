package DSSimProtocol.ProtocolHandler;

import DSSimProtocol.Action;
import DSSimProtocol.UnrecognisedCommandException;

public interface ProtocolHandler {

    public Action onEnterState();

    public abstract Action onReceiveMessage(String message) throws UnrecognisedCommandException;
}
