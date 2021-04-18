package DSSimProtocol.ProtocolHandler;

import DSSimProtocol.Action;
import DSSimProtocol.UnrecognisedCommandException;

public class FinalProtocolHandler implements ProtocolHandler {

    @Override
    public Action onEnterState() {
        return new Action(Action.ActionIntent.SEND_MESSAGE, "QUIT");
    }

    @Override
    public Action onReceiveMessage(String message) throws UnrecognisedCommandException {
        switch (message) {
            case "QUIT" -> {
                return new Action(Action.ActionIntent.QUIT);
            }
        
            default -> {
                throw new UnrecognisedCommandException("Unrecognised command: " + message);
            }
        }
    }
}
