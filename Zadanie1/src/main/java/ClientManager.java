import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientManager implements Runnable, Hook {

    private final Socket clientSocket;
    private final PrintWriter out;
    private final InputStreamReader in;
    private final LineManager lm;

    public ClientManager(Socket clientSocket) throws IOException{
        this.clientSocket = clientSocket;
        out = new PrintWriter(clientSocket.getOutputStream());
        in = new InputStreamReader(clientSocket.getInputStream());
        lm = new LineManager(in);
        addHook();
    }

    public void run(){
        Thread lineUpdater = new Thread(lm);
        lineUpdater.start();

        while(!lm.isTimeout() || lm.hasMoreLines()){
            synchronized(lm){
                boolean status = respond();
                if(!status)
                    waitingState();
            }
        }
        stopConnection();
    }

    private boolean respond(){
        Line newLine = lm.getLine();

        if(newLine == null) return false;
        if(!newLine.getErrFlag())
            sendComputedMessage(newLine.getLine());
        else 
            sendErrorMessage();
        
        return true;
    }

    private void sendComputedMessage(String line){
        long result = 0;
        String[] numbers = line
            .substring(0, line.length()-2)
            .split(" ");
            try {
                for(String number : numbers){
                    long nextVal = Long.parseLong(number);
                    if(Long.MAX_VALUE - nextVal < result)
                        throw new NumberFormatException();
                    result += nextVal;
                }
                out.print(result + "\r\n");
                out.flush();
            } catch (NumberFormatException e) {
                sendErrorMessage();
            }
        
    }

    private void sendErrorMessage(){
        out.print("ERROR\r\n");
        out.flush();
    }

    private void waitingState(){
        try {
            lm.wait();
        } catch (InterruptedException ignored) {}
    }

    public void stopConnection(){    
        try { 
            if(in != null)
                in.close();
            if(out != null)
                out.close();
            if(clientSocket != null)
                clientSocket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}