package Protocol;

import java.io.*;
import java.net.Socket;

// public class Server implements Runnable {
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

    // String message = "";
    // @Override
    // public void run() {
    //     StringBuilder result = new StringBuilder();
    //     // dont know if this logic is correct or even needed.
    //     // if bytes currently avaliable is greater than 0 then loop.
    //     // int bytesAvaliable = socket.getInputStream().available();
    //     // while (bytesAvaliable > 0) {
    //     // bytesAvaliable = socket.getInputStream().available();
    //     while (true) {
    //         int read = (int) '\n';
    //         try {
    //             read = inStream.read();
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //         }
    //         if (read == (int) '\n') {
    //             break;
    //         }
    //         result.append((char) read);
    //     }
    //     String message = result.toString();
    //     message = message.replace("\n", "").replace("\r", "");
        
    //     if (verbose) System.out.println("RECV: " + message);
    // }

    // public String returnRead(Boolean recieveData) {
    //     if (recieveData) {
    //         return message;
    //     } else 
    //         return "";
    // }
    
    /**
     * Reads a message from the server. This function is Blocking
     * Use for any sequential order of server reads is needed
     * Mainly for the initialisation steps of the client and server
     * @return returns a message as a String without newlines
     * @throws IOException IO Reading Error
     */
    public String readStringBlocking(Boolean recieveData) throws IOException {
        if (recieveData) {
            StringBuilder result = new StringBuilder();
            // dont know if this logic is correct or even needed.
            // if bytes currently avaliable is greater than 0 then loop.
            // int bytesAvaliable = socket.getInputStream().available();
            // while (bytesAvaliable > 0) {
            // bytesAvaliable = socket.getInputStream().available();
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
        } else 
            return "";
    }

    // // (todo) implement multithreading to be read to a buffer/queue!
    // /**
    //  * Reads a message from the server. This function is Non-Blocking and will be performed on a seperate thread
    //  * Use when any non-sequential reading
    //  * Mainly used for reading job data while also performing calculations
    //  * @return returns a message as a String without newlines
    //  * @throws IOException IO Reading Error
    //  */
    // public String readStringNonBlocking() throws IOException {
    //     // if (inStream.ready()) {
    //         StringBuilder result = new StringBuilder();
    //         int bytesAvaliable = socket.getInputStream().available();
    //         while (bytesAvaliable > 0) {
    //             int read = inStream.read();
    //             if (read == (int) '\n') {
    //                 break;
    //             }
    //             result.append((char) read);
    //         }
    //         String message = result.toString();
    //         message = message.replace("\n", "").replace("\r", "");
            
    //         if (verbose) System.out.println("RECV: " + message);
    //         return message;
    //     // } else {
    //     //     if (verbose) System.out.println("RECV: " + "");
    //     //     return "";
    //     // }
    // }

}
