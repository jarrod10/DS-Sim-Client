public class Event_Handling {

    public static void mainLoop () {
        while (true) {

            // Undertaken action returned by the protocol handler
            switch (action.intent) {

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
