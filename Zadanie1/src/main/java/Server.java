import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server implements Hook {

    public final static int MAX_REQUEST_SIZE = 1024;
    public final static int TIMEOUT_MILLIS = 5000;
    private final int PORT = 2020;

    private final ServerSocket so;

    public Server() throws IOException{
        so = new ServerSocket(PORT);
    }

    private void startServer() throws IOException{
        addHook();
        // System.out.println("Server started");
        while(true)
            acceptClient();
    }

    private void acceptClient() throws IOException{
        final Socket clientSocket = so.accept(); 
        clientSocket.setSoTimeout(TIMEOUT_MILLIS);
        ClientManager clientManager;
        try {
            clientManager = new ClientManager(clientSocket);
            Thread t = new Thread(clientManager);
            t.start();
            // System.out.println("\nNew client accepted!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopConnection(){
        try {
            so.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
    }
}