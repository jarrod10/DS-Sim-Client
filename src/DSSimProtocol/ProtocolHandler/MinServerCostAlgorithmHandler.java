package DSSimProtocol.ProtocolHandler;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import DSSimProtocol.Action;
import DSSimProtocol.Job;
import DSSimProtocol.ProtocolState;
import DSSimProtocol.Server;
import DSSimProtocol.SystemInformation;
import DSSimProtocol.UnrecognisedCommandException;
import DSSimProtocol.XMLParser;
import DSSimProtocol.Server.ServerState;

public class MinServerCostAlgorithmHandler implements ProtocolHandler {

    boolean MultiPart = false;
    int nRecs = 0;
    int count = 0;
    List<Server> avaliableServers;
    Job job;

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

        if (!MultiPart) {
            switch (messageParts[0]) {
    
                case "OK", "JCPL" -> {
                    return new Action(Action.ActionIntent.SEND_MESSAGE, "REDY");
                }
    
                case "JOBN" -> {
                    job = new Job(messageParts);
                    return new Action(Action.ActionIntent.SEND_MESSAGE, "GETS Capable " + job.cpu + " " + job.memory + " " + job.disk);
                }

                case "DATA" -> {
                    MultiPart = true;
                    count = 0;
                    nRecs = Integer.parseInt(messageParts[1]);
                    avaliableServers = new ArrayList<Server>(nRecs);
                    return new Action(Action.ActionIntent.SEND_MESSAGE, "OK");
                }

                // here is the code that runs when all server data is finished sending and scheduling decision has to be made
                case "." -> {
                    return new Action(Action.ActionIntent.COMMAND_SCHD, job, avaliableServers.get(0));
                }
    
                case "NONE" -> {
                    return new Action(Action.ActionIntent.SWITCH_STATE, ProtocolState.QUITTING);
                }
    
                default -> {
                    throw new UnrecognisedCommandException("Unrecognised command: " + message);
                }
    
            }
        } else {
            String serverType = messageParts[0];
            int serverID = Integer.parseInt(messageParts[1]);
            ServerState serverState = Server.ServerState.valueOf(messageParts[2].toUpperCase());
            int curStartTime = Integer.parseInt(messageParts[3]);
            int core = Integer.parseInt(messageParts[4]);
            int mem = Integer.parseInt(messageParts[5]);
            int disk = Integer.parseInt(messageParts[6]);
            avaliableServers.add(new Server(serverType, serverID, serverState, curStartTime, core, mem, disk));
            count++;

            if (count >= nRecs) {
                MultiPart = false;
                return new Action(Action.ActionIntent.SEND_MESSAGE, "OK");
            }
            return new Action(Action.ActionIntent.MULTIPART);
        }
    }
}
