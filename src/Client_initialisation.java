import Protocol.*;
import Protocol.ConcreteHandler.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;

class Client {

    // Initialise properties to sensible defaults
    static boolean verbose = false;
    static boolean debug = false;
    static String configurationPath = "ds-system.xml";
    static String algorithmName = "allToLargest";
    static String remoteAddress = "127.0.0.1";
    static int port = 50000;

    public static void main(String[] args) throws Exception {

        // Handle command line arguments
        for (int i = 0; i < args.length; i++) {
            String argument = args[i];
            switch (argument) {
                case "-v" -> verbose = true;
                case "-d" -> debug = true;
                case "-ip" -> {
                    try {
                        remoteAddress = args[++i];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("FATAL: No IP address given with '-ip' argument");
                        System.exit(-1);
                    }
                }
                case "-port" -> {
                    try {
                        port = Integer.parseInt(args[++i]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("FATAL: No port given with '-p' argument");
                        System.exit(-1);
                    } catch (NumberFormatException e) {
                        System.out.println("FATAL: Port not a recognised number");
                        System.exit(-1);
                    }
                }
                case "-a" -> {
                    // TODO Actually switch algorithms if argument is present
                    try {
                        algorithmName = args[++i];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("FATAL: No algorithm provided with '-a' argument");
                        System.exit(-1);
                    }
                }
                case "-c" -> {
                    try {
                        configurationPath = args[++i];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("FATAL: No path provided with '-c' argument");
                        System.exit(-1);
                    }
                }
                default -> System.out.println("ERROR: Unrecognised argument: " + argument + ", ignoring");
            }
        }

        // Read system information XML if possible
        try {
            SystemInfomation info = XMLParser.parse(configurationPath);
        } catch (FileNotFoundException e) {
            // TODO Fall back on protocol based system discovery if XML error
            System.out.println("FATAL: XML file " + configurationPath + " does not exist");
            System.exit(-1);
        }

        // Attempt to create connection to ds-sim server
        Connection remoteConnection = null;
        try {
            remoteConnection = new Connection(remoteAddress, port, verbose, debug);
        } catch (IOException e) {
            System.out.println("FATAL: Could not create a connection to DS-Sim server");
            System.exit(-1);
        }

        // Create all necessary objects, and send the first message to the server
        ProtocolState protocolState = ProtocolState.DEFAULT;
        ProtocolHandler protocolHandler = new DefaultProtocolHandler();

        ArrayDeque<Action> actionQueue = new ArrayDeque<>();
        actionQueue.add(protocolHandler.onEnterState());

        SystemInfomation systemInfomation = SystemInfomation.getInstance();

        while (true) {

            if (actionQueue.isEmpty()) {

                // Attempt to read data from server if available
                String recievedMessage = "";
                if (remoteConnection.readReady()) {
                    recievedMessage = remoteConnection.readString();
                }

                // Pass string to ProtocolHandler
                if (recievedMessage.length() > 0) {
                    actionQueue.add(protocolHandler.onReceiveMessage(recievedMessage));
                }

            } else {

                Action nextAction = actionQueue.removeFirst();
                switch (nextAction.intent) {

                    case SWITCH_STATE -> {
                        if (debug) System.out.println("SWITCHING STATE: " + nextAction.state);
                        switch (nextAction.state) {
                            case DEFAULT -> {
                                protocolState = ProtocolState.DEFAULT;
                                protocolHandler = new DefaultProtocolHandler();
                            }
                            case HANDSHAKING -> {
                                protocolState = ProtocolState.HANDSHAKING;
                                protocolHandler = new HandshakeProtocolHandler();
                            }
                            case AUTHENTICATING -> {
                                protocolState = ProtocolState.AUTHENTICATING;
                                protocolHandler = new AuthenticationProtocolHandler();
                            }
                            case EVENT_LOOP -> {
                                protocolState = ProtocolState.EVENT_LOOP;
                                protocolHandler = new EventLoopProtocolHandler();
                            }
                            case QUITTING -> {
                                protocolState = ProtocolState.QUITTING;
                                protocolHandler = new FinalProtocolHandler();
                            }
                        }
                        actionQueue.add(protocolHandler.onEnterState());
                    }
                    case SEND_MESSAGE -> {
                        remoteConnection.writeString(nextAction.message);
                    }
                    case COMMAND_SCHD -> {
                        remoteConnection.writeString("SCHD " +
                                nextAction.job.jobID + " " +
                                nextAction.server.serverType + " " +
                                nextAction.server.serverID);
                    }
                    case COMMAND_CNTJ -> {
                        remoteConnection.writeString("CNTJ " +
                                nextAction.server.serverType + " " +
                                nextAction.server.serverID + " " +
                                nextAction.jobState.getIndex());
                    }
                    case QUIT -> {
                        System.exit(1);
                    }
                }
            }
        }
    }

}