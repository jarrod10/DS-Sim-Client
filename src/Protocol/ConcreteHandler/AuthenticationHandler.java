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

        switch (message) {
            case "OK":
                return new Action(Intent.SWITCH_STATE, State.XML);
            default:
                throw new UnrecognisedCommandException("Unrecognised command: " + message);
        }

    }
}
