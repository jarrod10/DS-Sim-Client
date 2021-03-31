package Protocol.ConcreteHandler;

import Protocol.*;

public class EventLoopProtocolHandler implements ProtocolHandler {

    @Override
    public Action onEnterState() {
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
                Server server = SystemInfomation.mostCores(systemInfomation.serverList);

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
