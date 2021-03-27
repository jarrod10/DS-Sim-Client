package Protocol.ConcreteHandler;

import Protocol.Action;
import Protocol.ProtocolHandler;
import Protocol.Intent;
import Protocol.UnrecognisedCommandException;

public class FinalProtocolHandler implements ProtocolHandler {

    @Override
    public Action onEnterState() {
        return new Action(Intent.SEND_MESSAGE, "QUIT");
    }

    @Override
    public Action onReceiveMessage(String message) throws UnrecognisedCommandException {
        switch (message) {
            case "QUIT" -> {
                return new Action(Intent.QUIT);
            }
        
            default -> {
                throw new UnrecognisedCommandException("Unrecognised command: " + message);
            }
        }
    }
}
