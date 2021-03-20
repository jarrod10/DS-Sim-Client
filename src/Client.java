import Protocol.Action;
import Protocol.ConcreteHandler.*;
import Protocol.Handler;
import Protocol.Server;
import Protocol.State;

// import java.io.*;
// import java.net.*;
// import java.nio.charset.StandardCharsets;
// import java.util.Optional;

class client {
    
    static final String address = "127.0.0.1";
    static final int port = 50000;

    static boolean verbose = false;
    static boolean debug = false;

    public static void main(String[] args) throws Exception {

        // Handle command line arguments
        for (int i = 0; i < args.length; i++) {
            String argument = args[i];
            switch (argument) {
                case "-v" -> verbose = true;
                case "-d" -> debug = true;
                case "-a" -> {
                    try {
                        String algorithmName = args[++i];

                        switch (algorithmName) {
                            default -> {
                                System.out.println("FATAL: Unrecgnised algorithm: " + algorithmName);
                                System.exit(-1);
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("FATAL: No algorithm provided with '-a' argument");
                        System.exit(-1);
                    }
                }
                default -> {
                    System.out.println("FATAL: Unrecognised argument: " + argument);
                    System.exit(-1);
                }
            }
        }

        Server remoteServer = new Server(address, port, verbose, debug);

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
                case XML -> {protocolHandler = new XMLHandler(); new XMLParser().parse();}
                case EVENT_HANDLING -> protocolHandler = new EventHandlingHandler();
                // case QUITTING -> protocolHandler = new FinalStateHandler();

                default -> throw new IllegalArgumentException("Unexpected value: " + protocolState);
            }
            
            // Attemp to to write data to server
            action = protocolHandler.enterState();
            remoteServer.writeString(action.message);

            // Attempt to read data from server
            message = remoteServer.readStringBlocking( (protocolState == State.XML) ? false : true );
        }
        
        // enter into job handling part of the client
        eventHandler.mainLoop(remoteServer, message);

    }
}