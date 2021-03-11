import java.io.*;
import java.net.*;
import java.util.Optional;
import java.util.Properties;

class client {

    static boolean verbose = false;
    static int port = 5000;

    public static void main(String[] args) throws Exception {

        // Handle command line arguments
        for (int i = 0; i < args.length; i++) {
            String argument = args[i];
            switch (argument) {
                case "-v" -> verbose = true;
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

        Socket socket = new Socket("localhost", port);
        DataInputStream streamIn = new DataInputStream(socket.getInputStream());
        DataOutputStream streamOut = new DataOutputStream(socket.getOutputStream());

        Optional<String> errorReponse = null;
        if (!handShakeClient(streamIn, streamOut, errorReponse)) {
            System.out.println("Error occured in handshake between client and server");
            System.out.println("Server Responded with:" + errorReponse.get());
            System.exit(1);
        }

        String stringBuffer = "";
        while (!stringBuffer.equals("QUIT")) {
            /// Writing to socket State

            /// Check current state and branch to code to respond to server response.
            /// (todo) figure out how to represent states. havent given it much though yet
            ///




            streamOut.writeUTF(stringBuffer);
            streamOut.flush();

            if (verbose)
                System.out.println(stringBuffer);

            /// Reading from socket State
            stringBuffer = streamIn.readUTF();

            if (verbose)
                System.out.println(stringBuffer);
        }

        streamOut.close();
        streamIn.close();
        socket.close();
    }

    /** 
     * Performs the static handshaking between the client and server. 
     * @param streamIn DataStreamIn object
     * @param streamOut DataStreamOut object
     * @param errorReponse If handshake fails, errorResponse will contain the string of the error response from the server
     * @return Boolean of handshake state. True = success, False = fail
     * @throws Exception
     */


     /// Some error in here thats not allowing the client and server to connect successfully. Server isnt recieving the HELO and wont progress past that right now.
    private static boolean handShakeClient (DataInputStream streamIn, DataOutputStream streamOut, Optional<String> errorReponse) throws Exception {
        String stringBuffer = "HELO";
        streamOut.writeUTF("HELO");
        streamOut.flush();
        if (verbose)
            System.out.println("SEND " + stringBuffer);

        stringBuffer = streamIn.readUTF();
        if (!stringBuffer.equals("OK")) {
            Optional.of(stringBuffer);
            return false;
        }
        if (verbose)
            System.out.println("RCVD " + stringBuffer);

        stringBuffer = "AUTH " + System.getProperty("user.name");
        streamOut.writeUTF(stringBuffer);
        streamOut.flush();
        if (verbose)
            System.out.println("SEND " + stringBuffer);

        stringBuffer = streamIn.readUTF();
        if (!stringBuffer.equals("OK")) {
            Optional.of(stringBuffer);
            return false;
        }
        if (verbose)
            System.out.println("RCVD " + stringBuffer);

        return true;
    }
}