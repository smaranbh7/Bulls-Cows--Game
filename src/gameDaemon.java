import java.io.*;
import java.net.*;

public class gameDaemon {
    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket clientSocket;

        try {
            serverSocket = new ServerSocket(12345);

            while (true) {
                clientSocket = serverSocket.accept();
                gameThread gameSession = new gameThread(clientSocket);
                gameSession.start();
            }

        } catch (IOException e) {
            System.err.println("Could not listen on port: 12345.");
            System.exit(1);
        }
    }
}