import Protocol.State;
import Protocol.Handler;
import Protocol.Server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import Protocol.Action;
// import Protocol.ConcreteHandler.FinalStateHandler;

public class Event_Handling {
    final boolean verbose;
    final boolean debug;

    Server remoteServer;
    State protocolState;
    Handler protocolHandler;
    Action action;
    // String message = "";

    // create some kind of queue
    Queue<Job> jobQueue = new LinkedList<Job>();

    /**
     * Event_Handling constructor.
     * (todo) pass in anything from client that this class may need
     */
    Event_Handling (boolean _verbose, boolean _debug, Server _remoteServer, State _protocolState, Handler _protocolHandler, Action _action) {
        verbose = _verbose;
        debug = _debug;
        remoteServer = _remoteServer;
        protocolState = _protocolState;
        protocolHandler = _protocolHandler;
        action = _action;
    }

    /**
     * The main loop of handling jobs from the server
     * @throws IOException
     */
    public void mainLoop (String initialMessage) throws IOException {
        String message = initialMessage;
        // change to while not QUIT message
        loop:
        while (protocolState != State.QUITTING) {

            //Manipulate Data
            switch (message) {
                case "JOBN" -> jobQueue.add(new Job(message));
                case "NONE" -> {protocolState = State.QUITTING; break loop;}         
                // default -> {break loop;}
            }

            //Write out data
            System.out.println(jobQueue.peek().EstimatedRunTime);

            //Read from server
            message = remoteServer.readStringBlocking(true);



            // Undertaken action returned by the protocol handler
            // switch (action.intent) {

            //     case SEND_MESSAGE -> {*
            //         remoteServer.writeString(action.message);
            //         if (verbose) {
            //             System.out.println("SEND: " + action.message);
            //         }
            //     }

            // }

        }
    }
}
