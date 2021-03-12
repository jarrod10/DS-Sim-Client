package Protocol;

import java.io.*;
import java.net.Socket;

public class Server {

    private Socket socket;
    private BufferedReader inStream;
    private BufferedWriter outStream;

    public Server(String host, int port) {
        try {
            socket = new Socket(host, port);
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            socket = null;
        }
    }

    public void writeString(String message) throws IOException {
        if (message.endsWith("\n")) {
            outStream.write(message);
        } else {
            outStream.write(message + "\n");
        }
        outStream.flush();
    }

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
            return result.toString();
        } else {
            return "";
        }
    }

}
