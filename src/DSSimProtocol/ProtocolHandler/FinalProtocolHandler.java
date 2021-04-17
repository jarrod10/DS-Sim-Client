package DSSimProtocol.ProtocolHandler;

import DSSimProtocol.Action;
import DSSimProtocol.ActionIntent;
import DSSimProtocol.UnrecognisedCommandException;

public class FinalProtocolHandler implements ProtocolHandler {

    @Override
    public Action onEnterState() {
        return new Action(ActionIntent.SEND_MESSAGE, "QUIT");
    }

    @Override
    public Action onReceiveMessage(String message) throws UnrecognisedCommandException {
        switch (message) {
            case "QUIT" -> {
                return new Action(ActionIntent.QUIT);
            }
        
            default -> {
                throw new UnrecognisedCommandException("Unrecognised command: " + message);
            }
        }
    }
}
