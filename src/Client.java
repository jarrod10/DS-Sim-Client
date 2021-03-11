import java.io.*;
import java.net.*;

class client {
    public static void main(String[] args) throws Exception {
        boolean verbose = false;

        System.out.println("Client's Alive!");
        if (args.length > 0) {
            if (args.length > 1) {
                System.out.println("Too many args!");
                System.out.println("Closing");
                System.exit(0);
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
            
            String stringBuffer = "";
            while (!stringBuffer.equals("QUIT")) {
                /// Writing to socket State
                
                /// Check current state and branch to code to respond to server response.

                // if (stringBuffer.equals("HELO")) {
                //     stringBuffer = System.getProperty("user.name");
                // }

                stringBuffer = "HELO";



                streamOut.writeUTF(stringBuffer);
                streamOut.flush();

                if (verbose) {
                    System.out.println(stringBuffer);
                }
                
                /// Reading from socket State
                stringBuffer = streamIn.readUTF(); 

                if (verbose) {
                    System.out.println(stringBuffer);
                }
            }
        
            streamIn.close();
            streamOut.close();
            socket.close();
        }
    }
}