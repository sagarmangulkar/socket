import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String args[]) {
        if (args.length != 0){
            System.out.println("Kindly do not provide any command line arguments.");
        }
        ServerSocket socketServer = null;
        try {
            socketServer = new ServerSocket(0);
            System.out.println("Host Name: " + socketServer.getInetAddress().getHostName());
            System.out.println("Port number: " + socketServer.getLocalPort());
            ExecutorService executorService = Executors.newFixedThreadPool(100);
            while (true) {
                Socket socketClient = socketServer.accept();
                Runnable runnable = new MessageProcessorRunnable(socketClient);
                executorService.execute(runnable);
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + socketServer.getLocalPort() + " or listening for a connection");
            e.printStackTrace();
        }
    }
}
