public class Event_Handling {
    final boolean verbose;
    final boolean debug;

    // create some kind of queue

    /**
     * Event_Handling constructor.
     * (todo) pass in anything from client that this class may need
     */
    Event_Handling (boolean _verbose, boolean _debug) {
        verbose = _verbose;
        debug = _debug;
    }

    /**
     * The main loop of handling jobs from the server
     */
    public void mainLoop () {
        // change to while not QUIT message
        while (true) {

            // Undertaken action returned by the protocol handler
            // switch (action.intent) {

            //     case SEND_MESSAGE -> {
            //         remoteServer.writeString(action.message);
            //         if (verbose) {
            //             System.out.println("SEND: " + action.message);
            //         }
            //     }

            // }

        }

    }
}
