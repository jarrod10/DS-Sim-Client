package Protocol;

import java.io.*;
import java.net.Socket;

public class Server {

    private Socket socket;
    private BufferedReader inStream;
    private BufferedWriter outStream;

    static boolean verbose;
    static boolean debug;

    public Server(String host, int port, boolean _verbose, boolean _debug) {
        verbose = _verbose;
        debug = _debug;
        try {
            socket = new Socket(host, port);
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            socket = null;
        }
    }

    /**
     * Writes a message to the server, adds newline
     * @param message A String of the message to be sent
     * @throws IOException IO Writting error
     */
    public void writeString(String message) throws IOException {
        if (message != null) {
            if (message.endsWith("\n")) {
                outStream.write(message);
            } else {
                outStream.write(message + "\n");
            }
            if (verbose) System.out.println("SEND: " + message);
            outStream.flush();
        }
    }

    /**
     * reads a message from the server
     * @return returns a message as a String without newlines
     * @throws IOException IO Reading Error
     */
    public String readString() throws IOException {
        if (inStream.ready()) {
            StringBuilder result = new StringBuilder();
            while (true) {
                int read = inStream.read();
                if (read == (int) '\n') {
                    break;
                }
                result.append((char) read);
            }
            String message = result.toString();
            message = message.replace("\n", "").replace("\r", "");
            
            if (verbose) System.out.println("RECV: " + message);
            return message;
        } else {
            if (verbose) System.out.println("RECV: " + "");
            return "";
        }
    }

}
