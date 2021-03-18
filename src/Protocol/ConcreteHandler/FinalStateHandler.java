package Protocol.ConcreteHandler;

import Protocol.Action;
import Protocol.Handler;
import Protocol.Intent;
import Protocol.State;
import Protocol.UnrecognisedCommandException;

public class FinalStateHandler implements Handler {

    @Override
    public Action enterState() {
        return new Action(Intent.SEND_MESSAGE, "QUIT");
    }

    @Override
    public Action handleMessage(String message) throws UnrecognisedCommandException {
        switch (message) {
            case "QUIT" -> {
                // (todo) figure out better way of doing this!
                System.exit(0);
                return new Action(Intent.SWITCH_STATE, State.QUITTING);
            }
        
            default -> {
                throw new UnrecognisedCommandException("Unrecognised command: " + message);
            }
        }
    }
}
