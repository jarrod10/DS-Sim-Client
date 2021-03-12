package Protocol.ConcreteHandler;

import Protocol.*;


/**
 * The DefaultHandler is the handler that is used at startup. It's only job is to immediately switch
 * to another handler to process messages. This is done because the enterState
 */
public class DefaultHandler implements Handler {

    @Override
    public Action enterState() {
        return null;
    }

    @Override
    public Action handleMessage(String message) throws UnrecognisedCommandException {
        return new Action(Intent.SWITCH_STATE, State.HANDSHAKING);
    }
}
