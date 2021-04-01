package Protocol.ConcreteHandler;

import Protocol.*;

import java.io.FileNotFoundException;

public class EventLoopProtocolHandler implements ProtocolHandler {

    @Override
    public Action onEnterState() {

        // Read system information XML if possible
        try {
            SystemInfomation info = XMLParser.parse(SystemInfomation.configurationPath);
        } catch (FileNotFoundException e) {
            // TODO Fall back on protocol based system discovery if XML error
            System.out.println("FATAL: XML file " + SystemInfomation.configurationPath + " does not exist");
            System.exit(-1);
        }

        return new Action(Intent.SEND_MESSAGE, "REDY");
    }

    @Override
    public Action onReceiveMessage(String message) throws UnrecognisedCommandException {

        String[] messageParts = message.split(" ");
        SystemInfomation systemInfomation = SystemInfomation.getInstance();

        switch (messageParts[0]) {
            case "OK" -> {
                return new Action(Intent.SEND_MESSAGE, "REDY");
            }
            case "JOBN" -> {
                Job job = new Job(messageParts);

                //Grabs largest server from server list
                Server server = SystemInfomation.mostCores();

                return new Action(Intent.COMMAND_SCHD, job, server);
            }
            case "JCPL" -> {
                return new Action(Intent.SEND_MESSAGE, "REDY");
            }
            case "NONE" -> {
                return new Action(Intent.SWITCH_STATE, ProtocolState.QUITTING);
            }
            default -> {
                throw new UnrecognisedCommandException("Unrecognised command: " + message);
            }
        }
    }
}
