import Protocol.State;
import Protocol.Handler;
import Protocol.Server;
import Protocol.Action;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;



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
        
        SystemInfomation s = SystemInfomation.getInstance();
        int c = 0;
        
        // change to while not QUIT message
        loop:
        while (protocolState != State.QUITTING) {
            String[] messageTokens = message.split(" ");
            
            //Manipulate Data
            switch (messageTokens[0]) {
                case "JOBN" -> {jobQueue.add(new Job(messageTokens));
                                    remoteServer.writeString("SCHD " +c+ " "+ s.servers.get(2).get(0) + " 0");}
                case "JCPL" -> {remoteServer.writeString("TERM");}
                case "NONE" -> {protocolState = State.QUITTING; break loop;}         
                // default -> {break loop;}
            }

            // example of accessing data
            // System.out.println(jobQueue.peek().EstimatedRunTime);

            //Write out data
            
            c++;

            // System.out.println(s.servers.get(s.serverCount - 1).get(4));
            
            
            //Read from server
            message = remoteServer.readStringBlocking(true);

            remoteServer.writeString("REDY");
            message = remoteServer.readStringBlocking(true);
            // remoteServer.writeString("REDY");

        }
    }
}
