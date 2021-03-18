package Protocol.ConcreteHandler;

import Protocol.*;

public class HandshakeHandler implements Handler {

    @Override
    public Action enterState() {
        return new Action(Intent.SEND_MESSAGE, "HELO");
    }

    @Override
    public Action handleMessage(String message) throws UnrecognisedCommandException {

        switch (message) {
            case "OK" -> {
                return new Action(Intent.SWITCH_STATE, State.QUITTING);
            }
            default -> {
                throw new UnrecognisedCommandException("Unrecognised command: " + message);
            }
        }

    }
}
