package DSSimProtocol.ProtocolHandler;

import DSSimProtocol.Action;
import DSSimProtocol.ProtocolState;
import DSSimProtocol.UnrecognisedCommandException;

public class HandshakeProtocolHandler implements ProtocolHandler {

    @Override
    public Action onEnterState() {
        return new Action(Action.ActionIntent.SEND_MESSAGE, "HELO");
    }

    @Override
    public Action onReceiveMessage(String message) throws UnrecognisedCommandException {

        return switch (message) {
            case "OK" -> new Action(Action.ActionIntent.SWITCH_STATE, ProtocolState.AUTHENTICATING);
            default -> throw new UnrecognisedCommandException("Unrecognised command: " + message);
        };
    }
}
