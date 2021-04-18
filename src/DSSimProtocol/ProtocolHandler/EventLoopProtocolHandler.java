package DSSimProtocol.ProtocolHandler;

import DSSimProtocol.*;
import DSSimProtocol.SystemInformation;

import java.io.FileNotFoundException;

public class EventLoopProtocolHandler implements ProtocolHandler {

    @Override
    public Action onEnterState() {

        // Read system information XML if possible
        try {
            XMLParser.parse(SystemInformation.configurationPath);
        } catch (FileNotFoundException e) {
            java.lang.System.out.println("FATAL: XML file " + SystemInformation.configurationPath + " does not exist");
            java.lang.System.exit(-1);
        }

        return new Action(Action.ActionIntent.SEND_MESSAGE, "REDY");
    }

    @Override
    public Action onReceiveMessage(String message) throws UnrecognisedCommandException {

        String[] messageParts = message.split(" ");

        switch (messageParts[0]) {

            case "OK", "JCPL" -> {
                return new Action(Action.ActionIntent.SEND_MESSAGE, "REDY");
            }

            case "JOBN" -> {
                Job job = new Job(messageParts);

                //Grabs largest server from server list
                Server server = SystemInformation.mostCores();

                return new Action(Action.ActionIntent.COMMAND_SCHD, job, server);
            }

            case "NONE" -> {
                return new Action(Action.ActionIntent.SWITCH_STATE, ProtocolState.QUITTING);
            }

            default -> {
                throw new UnrecognisedCommandException("Unrecognised command: " + message);
            }

        }
    }
}
