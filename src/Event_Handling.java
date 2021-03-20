import Protocol.State;
import Protocol.Handler;
import Protocol.Server;
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
    // Queue jobQueue;

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
     */
    public void mainLoop (String initialMessage) {
        String message = initialMessage;
        // change to while not QUIT message
        while (protocolState != State.QUITTING) {

            //Read from server


            //Manipulate Data


            //Write out data

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
