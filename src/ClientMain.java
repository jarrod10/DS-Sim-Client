import DSSimProtocol.*;
import DSSimProtocol.ProtocolHandler.*;
import DSSimProtocol.SystemInformation;

import java.io.IOException;
import java.util.ArrayDeque;

class Client {

    public static void main(String[] args) throws Exception {


        // Handle command line arguments
        for (int i = 0; i < args.length; i++) {
            String argument = args[i];
            switch (argument) {
                case "-v" -> SystemInformation.verbose = true;
                case "-d" -> SystemInformation.debug = true;
                case "-ip" -> {
                    try {
                        SystemInformation.remoteAddress = args[++i];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        java.lang.System.out.println("FATAL: No IP address given with '-ip' argument");
                        java.lang.System.exit(-1);
                    }
                }
                case "-port" -> {
                    try {
                        SystemInformation.port = Integer.parseInt(args[++i]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        java.lang.System.out.println("FATAL: No port given with '-p' argument");
                        java.lang.System.exit(-1);
                    } catch (NumberFormatException e) {
                        java.lang.System.out.println("FATAL: Port not a recognised number");
                        java.lang.System.exit(-1);
                    }
                }
                case "-a" -> {
                    try {
                        SystemInformation.algorithmName = SystemInformation.Algorithms.valueOf(args[++i]);
                        // SystemInformation.algorithmName = args[++i];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        java.lang.System.out.println("FATAL: No algorithm provided with '-a' argument");
                        java.lang.System.exit(-1);
                    }
                }
                case "-c" -> {
                    try {
                        SystemInformation.configurationPath = args[++i];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        java.lang.System.out.println("FATAL: No path provided with '-c' argument");
                        java.lang.System.exit(-1);
                    }
                }
                default -> java.lang.System.out.println("ERROR: Unrecognised argument: " + argument + ", ignoring");
            }
        }


        // Attempt to create connection to ds-sim server
        Connection remoteConnection = null;
        try {
            remoteConnection = new Connection(
                    SystemInformation.remoteAddress,
                    SystemInformation.port);
        } catch (IOException e) {
            java.lang.System.out.println("FATAL: Could not create a connection to DS-Sim server");
            java.lang.System.exit(-1);
        }


        // Create all necessary objects, and send the first message to the server
        ProtocolHandler protocolHandler = new DefaultProtocolHandler();

        ArrayDeque<Action> actionQueue = new ArrayDeque<>();
        actionQueue.add(protocolHandler.onEnterState());


        // Main event loop for DSSimClient
        while (true) {

            if (actionQueue.isEmpty()) {

                // Attempt to read data from server if available
                String receivedMessage = "";
                if (remoteConnection.readReady()) {
                    receivedMessage = remoteConnection.readString();
                }

                // Pass string to ProtocolHandler
                if (receivedMessage.length() > 0) {
                    actionQueue.add(protocolHandler.onReceiveMessage(receivedMessage));
                }

            } else {

                // Carry out action returned by the protocolHandler
                Action nextAction = actionQueue.removeFirst();
                switch (nextAction.intent) {

                    case SWITCH_STATE -> {
                        if (SystemInformation.debug) java.lang.System.out.println("SWITCHING STATE: " + nextAction.state);

                        switch (nextAction.state) {
                            case DEFAULT -> protocolHandler = new DefaultProtocolHandler();
                            case HANDSHAKING -> protocolHandler = new HandshakeProtocolHandler();
                            case AUTHENTICATING -> protocolHandler = new AuthenticationProtocolHandler();
                            case EVENT_LOOP -> {
                                switch (SystemInformation.algorithmName) {
                                    case HolyGrail -> protocolHandler = new HolyGrailAlgorithmHandler();
                                    case Default -> protocolHandler = new DefaultEventLoopProtocolHandler();
                                    default -> {
                                        System.out.println("FATAL: No algorithm Avaliable");
                                        protocolHandler = new FinalProtocolHandler();
                                    }
                                }
                            }
                            case QUITTING -> protocolHandler = new FinalProtocolHandler();
                        }
                        
                        actionQueue.add(protocolHandler.onEnterState());
                    }
                    
                    case SEND_MESSAGE -> remoteConnection.writeString(nextAction.message);
                    case COMMAND_SCHD -> remoteConnection.writeString("SCHD " +
                    nextAction.job.jobID + " " +
                    nextAction.server.serverType + " " +
                    nextAction.server.serverID);
                    case COMMAND_CNTJ -> remoteConnection.writeString("CNTJ " +
                    nextAction.server.serverType + " " +
                    nextAction.server.serverID + " " +
                    nextAction.jobState.getIndex());
                    case COMMAND_MIGR -> remoteConnection.writeString(nextAction.message);
                    case MULTIPART -> {
                        // make some mini while loop to loop until multipart isn't needed anymore
                        // after while loop is completed make sure you do one more normal loop and then continue with normal code
                        // this is so it can return all the multipart loop stuff without having to change how the normal loop works for this special case.
                        
                        // boolean loopFinished = false;
                        int loopCount = 0;
                        String FinalString = "";
                        // while (!loopFinished) {
                            
                            
                            remoteConnection.writeString(nextAction.message);
                            switch (nextAction.source) {
                                case "GETS" -> {
                                    String[] messageParts = new String[3];
                                    // if (remoteConnection.readReady()) {  // commented out this non blocking check because it was causing issues 
                                        messageParts = remoteConnection.readString().split(" ");
                                    // }

                                    String message= "";
                                    remoteConnection.writeString("OK");
                                    while (loopCount < Integer.parseInt(messageParts[1])) {
                                        if (remoteConnection.readReady()) {
                                            message = remoteConnection.readString();
                                        }
                                        if (message.length() > 0) {
                                            loopCount++;
                                            FinalString += message + " ";
                                        }
                                    }
                                }
                                
                                case "LSTJ" -> {
                                    // String[] messageParts = new String[3];
                                    // if (remoteConnection.readReady()) {
                                    //     messageParts = remoteConnection.readString().split(" ");
                                    // }
                                    String message = "";

                                    // if (remoteConnection.readReady()) {
                                        // Thread.sleep(100);
                                        // message = 
                                        remoteConnection.readString();
                                        // message = remoteConnection.readString();
                                    // }
                                    remoteConnection.writeString("OK");
                                    
                                    // some bug where message != "." when message = "." is returning true, expression is returing opposite
                                    // System.out.println(message == ".");
                                    while (!message.equals(".")) {
                                        // if (remoteConnection.readReady()) {
                                            // Thread.sleep(100);
                                            message = remoteConnection.readString();
                                        // }

                                        if (!message.equals(".")) {
                                            // if (message.length() > 0 && message == ".") {
                                            // FinalString = "";
                                            loopCount++;
                                            // System.out.println(FinalString);
                                            FinalString += message + " ";
                                        // }
                                            remoteConnection.writeString("OK");
                                        }
                                    }
                                }

                                // case "LSTJ2" -> {
                                //     String message= "";
                                //     if (remoteConnection.readReady()) {
                                //         message = remoteConnection.readString();
                                //     }
                                // }
                                 
                                default -> {break;}
                            }

                            
                            // String receivedMessage = "";
                            // if (remoteConnection.readReady()) {
                            //     receivedMessage = remoteConnection.readString();
                            // }
                            
                            // if (receivedMessage.length() > 0) {
                            //     actionQueue.add(protocolHandler.onReceiveMessage(receivedMessage));
                            // }
                        // }

                        // perform final part of multipart
                        actionQueue.add(protocolHandler.onReceiveMessage(FinalString + nextAction.source));
                    }
                    case QUIT -> System.exit(1);
                    default -> throw new IllegalArgumentException("Unexpected value: " + nextAction.intent);
                }
            }
        }

    }

}