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

public class HolyGrailAlgorithmHandler implements AlgorithmProtocolHandler {

    boolean MultiPart = false;
    EmultiPart emultiPart = EmultiPart.NULL;
    int nRecs = 0;
    int count = 0;
    List<Server> avaliableServers = null;
    Job job;
    
    enum EmultiPart {
        NULL,
        GETS,
        LSTJ,
    }

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
                    emultiPart = EmultiPart.GETS;
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
                // IDEA:
                // Use LSTJ to check what servers have waiting jobs and for every waiting job we check its core requirement.
                // at this point we also have the avaliable servers from the gets command which includes the servers remaining cores
                // we can use this infomation to then loop through all waiting jobs and use the MIGR (migrate job) to the (first, possibly but not exactly sure yet) server
                // so it fits better in another server

                case "." -> {
                    Server server = lowestCost();
                    // return new Action(Action.ActionIntent.COMMAND_SCHD, job, avaliableServers.get(0));
                    return new Action(Action.ActionIntent.SEND_MESSAGE, "LSTJ " + job.cpu + " " + job.memory + " " + job.disk); // send LSTJ command
                }
    
                case "NONE" -> {
                    return new Action(Action.ActionIntent.SWITCH_STATE, ProtocolState.QUITTING);
                }
    
                default -> {
                    throw new UnrecognisedCommandException("Unrecognised command: " + message);
                }
    
            }
        } else {
            switch (emultiPart) {
                case GETS -> {
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
                        emultiPart = EmultiPart.NULL;
                        return new Action(Action.ActionIntent.SEND_MESSAGE, "OK");
                    }
                }
                
                case LSTJ -> {
                    
                }
                
                case NULL -> {
                    
                }
                
                default -> {break;}
                
            }
            return new Action(Action.ActionIntent.MULTIPART);

        // end of switch statement
        }
    }

    /**
     * Finds the server with the lowest opperating cost 
     * 
     * @return the server with the lowest cost to operate
     */
    Server lowestCost() {
        // avaliableServers.
        List<Server> servers = new ArrayList<Server>();
        List<Server> AllServers = SystemInformation.serverList;
        for (Server server : avaliableServers) {
            // servers.add(AllServers.)
        }
        return null;
    }
}
