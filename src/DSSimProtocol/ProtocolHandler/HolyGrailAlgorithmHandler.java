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
import DSSimProtocol.Job.JobState;
import DSSimProtocol.Server.ServerState;

public class HolyGrailAlgorithmHandler implements AlgorithmProtocolHandler {

    int nRecs = 0;
    int count = 0;
    int serverloopCount = 0; 
    List<avaliableServersStructure> avaliableServers = null;
    List<listedJobsStructure> listedJobs = new ArrayList<listedJobsStructure>();
    Job job;
    boolean firstTimeFlag = true;
    boolean justSchd = false;



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

        switch (messageParts[messageParts.length - 1]) {
            case "GETS" -> {
                avaliableServers = new ArrayList<avaliableServersStructure>((messageParts.length-1)/9);
                for (int i = 0; i < (messageParts.length-1); i+=9) {
                    String serverType = messageParts[i];
                    int serverID = Integer.parseInt(messageParts[i+1]);
                    ServerState serverState = Server.ServerState.valueOf(messageParts[i+2].toUpperCase());
                    int curStartTime = Integer.parseInt(messageParts[i+3]);
                    int core = Integer.parseInt(messageParts[i+4]);
                    int mem = Integer.parseInt(messageParts[i+5]);
                    int disk = Integer.parseInt(messageParts[i+6]);
                    avaliableServers.add(new avaliableServersStructure( new Server(serverType, serverID, serverState, curStartTime, core, mem, disk), Integer.parseInt(messageParts[i+7]), Integer.parseInt(messageParts[i+8])));
                }
                return new Action(Action.ActionIntent.SEND_MESSAGE, "OK");
            }

            case "LSTJ" -> {
                // here is where LSTJ server info comes in.
                // make decision on what to 

                // possibly determine if LSTJ for previous query was "." by checking messageparts.length > 1
                // this only works in this case as the message returned will only be "LSTJ"

                for (int i = 0; i < (messageParts.length-2); i+=9) {
                    int jobID = Integer.parseInt(messageParts[i+1]);
                    Job.JobState jobState = JobState.valueOf(((Integer.parseInt(messageParts[i+2]) == 1) ? JobState.WAITING.toString() : JobState.RUNNING.toString()));
                    int estRuntime = Integer.parseInt(messageParts[i+4]);
                    int cpu = Integer.parseInt(messageParts[i+5]);
                    int memory = Integer.parseInt(messageParts[i+6]);
                    int disk = Integer.parseInt(messageParts[i+7]);

                    listedJobs.get(serverloopCount-1).childList.add(new Job(jobID, jobState, estRuntime, cpu, memory, disk));
                }
                if (serverloopCount >= avaliableServers.size()) {
                    listedJobs.add(new listedJobsStructure());
                    return new Action(Action.ActionIntent.MULTIPART, "LSTJ", "GETS Type " + avaliableServers.get(serverloopCount++-1).server.serverType);
                } else {
                    for (int i = 0; i < listedJobs.size(); i++) {
                        if (listedJobs.get(i).childList.size() > 1) {
                            Server tgtServer = avaliableServers.get(avaliableServers.size()-1).server;
                            for (int j = avaliableServers.size(); j >= 0; j--) {
                                if (j != i)
                                    tgtServer = avaliableServers.get(j).server;
                            }
                            return new Action(Action.ActionIntent.COMMAND_MIGR, "MIGR " + job.jobID + " " + listedJobs.get(i).ServerType + " " + listedJobs.get(i).ServerID + " " + tgtServer.serverType + " " + tgtServer.serverID);
                        }
                    // return new Action(Action.ActionIntent.MULTIPART, "LSTJ", "GETS Type " + avaliableServers.get(serverloopCount++-1).serverType);
                    }
                    return new Action(Action.ActionIntent.SEND_MESSAGE, "REDY");
                }
            }
            
            default -> {
                switch (messageParts[0]) {

                    case "OK", "JCPL" -> {
                        if (justSchd) {
                            justSchd = false;
                            Action action = new Action(Action.ActionIntent.MULTIPART, "LSTJ", "LSTJ " + avaliableServers.get(serverloopCount).server.serverType +  " " + avaliableServers.get(serverloopCount).server.serverID);
                            serverloopCount++;
                            listedJobs.add(new listedJobsStructure());
                            return action;
                        } else {
                            return new Action(Action.ActionIntent.SEND_MESSAGE, "REDY");
                        }
                    }

                    case "JOBN" -> {
                        job = new Job(messageParts);
                        return new Action(Action.ActionIntent.MULTIPART, "GETS", "GETS Capable " + job.cpu + " " + job.memory + " " + job.disk);
                    }


                    ////////////////////////////////
                    // case "DATA" -> {
                    //     nRecs = Integer.parseInt(messageParts[1]);
                    //     switch (emultiPart) {
                    //         case GETS -> {
                    //             avaliableServers = new ArrayList<Server>(nRecs);
                    //         }

                    //         case LSTJ -> {}

                    //         default -> {break;}
                    //     }

                    //     return new Action(Action.ActionIntent.SEND_MESSAGE, "OK");
                    // }
                    ///////////////////////////////


                    // here is the code that runs when all server data is finished sending and scheduling decision has to be made
                    // IDEA:
                    // Use LSTJ to check what servers have waiting jobs and for every waiting job we check its core requirement.
                    // at this point we also have the avaliable servers from the gets command which includes the servers remaining cores
                    // we can use this infomation to then loop through all waiting jobs and use the MIGR (migrate job) to the (first, possibly but not exactly sure yet) server
                    // so it fits better in another server

                    // gets job
                    // gets avaliable servers
                    // schedule job
                    // check all servers for multiple waiting jobs and if so migrate a job
                    case "." -> {
                        // schedule job first and then perform LSTJ
                        if (firstTimeFlag) {
                            firstTimeFlag = false;
                            justSchd = true;
                            return new Action(Action.ActionIntent.COMMAND_SCHD, job, avaliableServers.get(0).server);
                        } else {
                            // return new Action(Action.ActionIntent.MULTIPART, "LSTJ", "LSTJ " + avaliableServers.get(serverloopCount++).serverType +  " " + avaliableServers.get(serverloopCount++).serverID);
                        }
                    }

                    case "GETS" -> {
                        String serverType = messageParts[0];
                        int serverID = Integer.parseInt(messageParts[1]);
                        ServerState serverState = Server.ServerState.valueOf(messageParts[2].toUpperCase());
                        int curStartTime = Integer.parseInt(messageParts[3]);
                        int core = Integer.parseInt(messageParts[4]);
                        int mem = Integer.parseInt(messageParts[5]);
                        int disk = Integer.parseInt(messageParts[6]);
                        avaliableServers.add(new avaliableServersStructure( new Server(serverType, serverID, serverState, curStartTime, core, mem, disk),1 ,1));
                        count++;
            
                        if (count >= nRecs) {
                            count = 0;
                            return new Action(Action.ActionIntent.SEND_MESSAGE, "OK");
                        }
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
        return null; 
    }
        // else {
        //     switch (emultiPart) {
        //         // case GETS -> {
        //         //     String serverType = messageParts[0];
        //         //     int serverID = Integer.parseInt(messageParts[1]);
        //         //     ServerState serverState = Server.ServerState.valueOf(messageParts[2].toUpperCase());
        //         //     int curStartTime = Integer.parseInt(messageParts[3]);
        //         //     int core = Integer.parseInt(messageParts[4]);
        //         //     int mem = Integer.parseInt(messageParts[5]);
        //         //     int disk = Integer.parseInt(messageParts[6]);
        //         //     avaliableServers.add(new Server(serverType, serverID, serverState, curStartTime, core, mem, disk));
        //         //     count++;
        
        //         //     if (count >= nRecs) {
        //         //         MultiPart = false;
        //         //         count = 0;
        //         //         // if (serverloopCount == avaliableServers.size()) {
        //         //         //     emultiPart = EmultiPart.NULL;
        //         //         // } else {
        //         //             // }
        //         //         // emultiPart = EmultiPart.LSTJ;
        //         //         return new Action(Action.ActionIntent.SEND_MESSAGE, "OK");
        //         //     }
        //         // }
                
        //         case LSTJ -> {


        //             // count++;
                    
        //             // if (count >= nRecs) {
        //                 //     // count = 0;
        //                 //     emultiPart = EmultiPart.NULL;
        //                 // }
        //             MultiPart = false;
        //             // return new Action(Action.ActionIntent.SEND_MESSAGE, "OK");
        //         }
                
        //         case NULL -> {
                    
        //         }
                
        //         default -> {break;}
                
        //     }
        //     return new Action(Action.ActionIntent.MULTIPART);

        // // end of switch statement
        // }
    // }

    // /**
    //  * Finds the server with the lowest opperating cost 
    //  * 
    //  * @return the server with the lowest cost to operate
    //  */
    // Server lowestCost() {
    //     // avaliableServers.
    //     List<Server> servers = new ArrayList<Server>();
    //     List<Server> AllServers = SystemInformation.serverList;
    //     for (Server server : avaliableServers) {
    //         // servers.add(AllServers.)
    //     }
    //     return null;
    // }

    /**
     * InnerHolyGrailAlgorithmHandler
     */
    public class listedJobsStructure {
        public List<Job> childList = new ArrayList<Job>();
        public String ServerType;
        public int ServerID;
    }

    public class avaliableServersStructure {
        public Server server;
        public int waitingJobs;
        public int runningJobs;

        public avaliableServersStructure(Server Server, int WaitingJobs, int RunningJobs) {
            server = Server;
            waitingJobs = WaitingJobs;
            runningJobs = RunningJobs;
        }
    }
}
