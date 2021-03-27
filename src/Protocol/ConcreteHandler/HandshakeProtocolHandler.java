package Protocol.ConcreteHandler;

import Protocol.Action;
import Protocol.ProtocolHandler;
import Protocol.Intent;
import Protocol.ProtocolState;
import Protocol.UnrecognisedCommandException;

public class HandshakeProtocolHandler implements ProtocolHandler {

    @Override
    public Action onEnterState() {
        return new Action(Intent.SEND_MESSAGE, "HELO");
    }

    @Override
    public Action onReceiveMessage(String message) throws UnrecognisedCommandException {

        return switch (message) {
            case "OK" -> new Action(Intent.SWITCH_STATE, ProtocolState.AUTHENTICATING);
            default -> throw new UnrecognisedCommandException("Unrecognised command: " + message);
        };
    }
}
