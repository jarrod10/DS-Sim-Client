package Protocol.ConcreteHandler;

import Protocol.Action;
import Protocol.Handler;
import Protocol.Intent;
import Protocol.State;
import Protocol.UnrecognisedCommandException;

public class XMLHandler implements Handler {

    @Override
    public Action enterState() {
        // call XML handling functions

        return new Action();
    }


    @Override
    public Action handleMessage(String message) throws UnrecognisedCommandException {
        return new Action(Intent.SWITCH_STATE, State.EVENT_HANDLING);
    }
    
}
