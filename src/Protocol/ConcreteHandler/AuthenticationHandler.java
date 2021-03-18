package Protocol.ConcreteHandler;

import Protocol.*;

public class AuthenticationHandler implements Handler {

    @Override
    public Action enterState() {
        return new Action(Intent.SEND_MESSAGE, "AUTH TestUsername");
    }

    @Override
    public Action handleMessage(String message) throws UnrecognisedCommandException {

        switch (message) {
            case "OK":
                return new Action(Intent.SWITCH_STATE, State.EVENT_HANDLING);
            default:
                throw new UnrecognisedCommandException("Unrecognised command: " + message);
        }

    }
}
