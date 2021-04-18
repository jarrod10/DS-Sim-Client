package DSSimProtocol.ProtocolHandler;

import DSSimProtocol.Action;
import DSSimProtocol.ProtocolState;
import DSSimProtocol.UnrecognisedCommandException;

public class AuthenticationProtocolHandler implements ProtocolHandler {

    @Override
    public Action onEnterState() {
        return new Action(Action.ActionIntent.SEND_MESSAGE, "AUTH " + System.getProperty("user.name"));
    }

    @Override
    public Action onReceiveMessage(String message) throws UnrecognisedCommandException {

        return switch (message) {
            case "OK" -> new Action(Action.ActionIntent.SWITCH_STATE, ProtocolState.EVENT_LOOP);
            default -> throw new UnrecognisedCommandException("Unrecognised command: " + message);
        };
    }
}
