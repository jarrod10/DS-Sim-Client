package Protocol.ConcreteHandler;

import Protocol.Action;
import Protocol.ProtocolHandler;
import Protocol.Intent;
import Protocol.ProtocolState;
import Protocol.UnrecognisedCommandException;

public class AuthenticationProtocolHandler implements ProtocolHandler {

    @Override
    public Action onEnterState() {
        return new Action(Intent.SEND_MESSAGE, "AUTH TestUsername");
    }

    @Override
    public Action onReceiveMessage(String message) throws UnrecognisedCommandException {

        return switch (message) {
            case "OK" -> new Action(Intent.SWITCH_STATE, ProtocolState.EVENT_LOOP);
            default -> throw new UnrecognisedCommandException("Unrecognised command: " + message);
        };
    }
}
