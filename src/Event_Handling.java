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

    // Queue<Job> jobQueue = new LinkedList<Job>();
    Job job;

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
        
        loop:
        while (protocolState != State.QUITTING) {
            String[] messageTokens = message.split(" ");
            
            //Manipulate Data
            switch (messageTokens[0]) {
                case "OK" -> {/* do nothing and send REDY*/}
                case "JOBN" -> {job = new Job(messageTokens);
                                remoteServer.writeString("SCHD " + job.id + " "+ s.servers.get(2).get(0) + " 0"); /* Hardcoded for now */}
                case "JCPL" -> {/* Register internally that particular job has been completed and continue to send REDY*/}
                case "NONE" -> {protocolState = State.QUITTING; break loop;}         
            }

            // example of accessing data
            // System.out.println(jobQueue.peek().EstimatedRunTime);
            // System.out.println(s.servers.get(s.serverCount - 1).get(4));

            //Write out data
            message = remoteServer.readStringBlocking(true);

            remoteServer.writeString("REDY");
            
            //Read from server
            message = remoteServer.readStringBlocking(true);
        }
    }
}
