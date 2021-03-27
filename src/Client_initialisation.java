import Protocol.Action;
import Protocol.ConcreteHandler.*;
import Protocol.Handler;
import Protocol.Server;
import Protocol.State;

import java.io.IOException;
import java.sql.SQLOutput;

class client {

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
        } catch (IOException e) {
            // TODO Fall back on protocol based system discovery if XML error
            System.out.println("FATAL: XML file not found");
            System.exit(-1);
        }

        // Attempt to create connection to ds-sim server
        Server remoteServer = null;
        try {
            remoteServer = new Server(remoteAddress, port, verbose, debug);
        } catch (IOException e) {
            System.out.println("FATAL: Could not create a connection to DS-Sim server");
            System.exit(-1);
        }

        State protocolState = State.DEFAULT;
        Handler protocolHandler = new DefaultHandler();
        Action action = new Action();
        String message = "";
        Event_Handling eventHandler = new Event_Handling(verbose, debug, remoteServer, protocolState, protocolHandler, action);

        while (protocolState != State.EVENT_HANDLING) {  
            // Send message to protocol handler
            action = protocolHandler.handleMessage(message);
            protocolState = action.state;

            // Switching to next state
            if (debug) System.out.println("SWITCHING STATE: " + protocolState);
            switch (protocolState) {
                case HANDSHAKING -> protocolHandler = new HandshakeHandler();
                case AUTHENTICATING -> protocolHandler = new AuthenticationHandler();
                case EVENT_HANDLING -> protocolHandler = new EventHandlingHandler();
                // case QUITTING -> protocolHandler = new FinalStateHandler();

                default -> throw new IllegalArgumentException("Unexpected value: " + protocolState);
            }
            
            // Attemp to to write data to server
            action = protocolHandler.enterState();
            remoteServer.writeString(action.message);

            // Attempt to read data from server
            message = remoteServer.readStringBlocking(true);
        }
        
        // Enter into job handling part of the client
        eventHandler.mainLoop(message);

        // Terminate client
        protocolHandler = new FinalStateHandler();
        action = protocolHandler.enterState();
        protocolState = State.QUITTING;
        if (debug) System.out.println("SWITCHING STATE: " + protocolState);
        remoteServer.writeString(action.message);
    }
}