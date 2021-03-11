import java.io.*;
import java.net.*;
import java.util.Optional;

import javax.net.ssl.HandshakeCompletedEvent;

class client {
    static boolean verbose = false;
    public static void main(String[] args) throws Exception {

        System.out.println("Client's Alive!");
        if (args.length >= 0) {
            if (args.length > 1) {
                System.out.println("Too many args!");
                System.out.println("Closing");
                System.exit(1);
            }
            
            for (String arg : args) {
                if (arg.contains("-v") || arg.contains("-V")) {
                    System.out.println("Using Verbose Mode");
                    verbose = true;
                }                
            }

            Socket socket = new Socket("localhost", 50000);
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

                streamOut.writeUTF(stringBuffer);
                streamOut.flush();

                if (verbose)
                    System.out.println(stringBuffer);
                
                /// Reading from socket State
                stringBuffer = streamIn.readUTF(); 

                if (verbose)
                    System.out.println(stringBuffer);
            }
        
            streamIn.close();
            streamOut.close();
            socket.close();
        }
    }

    private static boolean handShakeClient (DataInputStream streamIn, DataOutputStream streamOut, Optional<String> errorReponse) throws Exception {
        String stringBuffer = "HELO";
        streamOut.writeUTF(stringBuffer);
        streamOut.flush();
        if (verbose)
            System.out.println(stringBuffer);

        stringBuffer = streamIn.readUTF();
        if (!stringBuffer.equals("OK")) {
            Optional.of(stringBuffer);
            return false;
        }
        if (verbose)
            System.out.println(stringBuffer);

        stringBuffer = System.getProperty("user.name");
        streamOut.writeUTF(stringBuffer);
        streamOut.flush();
        if (verbose)
            System.out.println(stringBuffer);

        stringBuffer = streamIn.readUTF();
        if (!stringBuffer.equals("OK")) {
            Optional.of(stringBuffer);
            return false;
        }
        if (verbose)
            System.out.println(stringBuffer);

        return true;
    }
}