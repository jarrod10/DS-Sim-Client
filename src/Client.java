import Protocol.Action;
import Protocol.ConcreteHandler.AuthenticationHandler;
import Protocol.ConcreteHandler.DefaultHandler;
import Protocol.ConcreteHandler.HandshakeHandler;
import Protocol.Handler;
import Protocol.Server;
import Protocol.State;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

class client {

    static boolean verbose = false;
    static boolean debug = false;
    static int port = 50000;

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

        Server remoteServer = new Server("127.0.0.1", 50000);

        State protocolState = State.DEFAULT;
        Handler protocolHandler = new DefaultHandler();
        Action action = new Action();

        while (true) {

            // Attempt to read data from server
            String message = remoteServer.readString();
            message = message.replace("\n", "").replace("\r", "");
            if (verbose) {
                System.out.println("RECV: " + message);
            }

            // Send message to protocol handler
            action = protocolHandler.handleMessage(message);

            // Undertaken action returned by the protocol handler
            switch (action.intent) {

                case SWITCH_STATE -> {
                    if (debug) { System.out.println("SWITCHING STATE: " + action.state);}
                    switch (action.state) {
                        case HANDSHAKING -> protocolHandler = new HandshakeHandler();
                        case AUTHENTICATING -> protocolHandler = new AuthenticationHandler();
                    }
                    protocolState = action.state;
                    Action stateChangeAction = protocolHandler.enterState();
                    if (stateChangeAction != null) {
                        remoteServer.writeString(stateChangeAction.message);
                        if (verbose) {
                            System.out.println("SEND: " + stateChangeAction.message);
                        }
                    }
                }

                case SEND_MESSAGE -> {
                    remoteServer.writeString(action.message);
                    if (verbose) {
                        System.out.println("SEND: " + action.message);
                    }
                }

            }

        }
    }
}