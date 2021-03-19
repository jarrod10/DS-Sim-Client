package Protocol.ConcreteHandler;

import Protocol.Action;
import Protocol.Handler;
import Protocol.Intent;
import Protocol.State;
import Protocol.UnrecognisedCommandException;

public class EventHandlingHandler implements Handler {

    @Override
    public Action enterState() {
        return new Action(Intent.SEND_MESSAGE, "REDY");
    }

    @Override
    public Action handleMessage(String message) throws UnrecognisedCommandException {
        // switch (message) {
        //     case "OK" -> {
        //         return new Action(Intent.LOOP, State.EVENT_HANDLING);
        //     }
        //     default -> {
        //         throw new UnrecognisedCommandException("Unrecognised command: " + message);
        //     }
        // }
        return new Action(Intent.LOOP, State.EVENT_HANDLING);
    }
}
