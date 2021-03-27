package Protocol.ConcreteHandler;

import Protocol.Action;
import Protocol.Handler;
import Protocol.Intent;
import Protocol.State;
import Protocol.UnrecognisedCommandException;

public class AuthenticationHandler implements Handler {

    @Override
    public Action enterState() {
        return new Action(Intent.SEND_MESSAGE, "AUTH TestUsername");
    }

    @Override
    public Action handleMessage(String message) throws UnrecognisedCommandException {

        return switch (message) {
            case "OK" -> new Action(Intent.SWITCH_STATE, State.EVENT_HANDLING);
            default -> throw new UnrecognisedCommandException("Unrecognised command: " + message);
        };

    }
}
