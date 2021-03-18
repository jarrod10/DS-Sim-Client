package Protocol.ConcreteHandler;

import Protocol.*;

public class FinalStateHandler implements Handler {

    @Override
    public Action enterState() {
        return new Action(Intent.SEND_MESSAGE, "QUIT");
    }

    @Override
    public Action handleMessage(String message) throws UnrecognisedCommandException {
        switch (message) {
            case "QUIT" -> {
                System.exit(0);
                return new Action(Intent.SWITCH_STATE, State.QUITTING);
            }
        
            default -> {
                throw new UnrecognisedCommandException("Unrecognised command: " + message);
            }
        }
    }
}
