import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.NoSuchElementException;


public class LineManager implements Runnable{

    private final LinkedList<Line> lines;
    private final InputStreamReader in;

    private boolean exit = false;


    public LineManager(InputStreamReader in){
        lines = new LinkedList<>();
        this.in = in;
    }

    public Line getLine(){
        try{ return lines.pop(); } 
        catch (NoSuchElementException e)
            { return null; }            
    }

    public boolean isTimeout(){
        return exit;
    }

    public boolean hasMoreLines(){
        return lines.size() != 0;
    }

    public void run(){
        while(!exit)
            updateList();
    }

    private void updateList(){
        while(hasMoreChars()){
            Line nextLine = getNextLine();
            synchronized(this){
                lines.add(nextLine);
                notify();
            }
        }
    }

    private boolean hasMoreChars(){
        try {
            return in.ready();
        } catch (IOException e) {
            // System.out.println("Ready exception: " + e.getMessage());
        }
        return false;
    }

    private Line getNextLine(){
        boolean errFlag;

        String lineStr = "";
        int currLength = 0;

        while(!exit){
            char c = getNextChar();
            lineStr += c;
            currLength++;
            if(isEndOfLine(lineStr, currLength, c)) 
                break;
            if(currLength > Server.MAX_REQUEST_SIZE){ 
                exit = true;
                return null;
            }
        }
        errFlag = !isLineValid(lineStr);
        if(exit) return null;
        return new Line(lineStr, errFlag);
    }

    private boolean isEndOfLine(String lineStr, int currLength, char currentChar){
        return (currLength >= 2) && (currentChar == '\n') &&
                (lineStr.charAt(lineStr.length() - 2) == '\r');
    }

    private boolean isCharacterValid(char c){
        return (c >= '0' && c <= '9') ||
               (c == '\r') ||
               (c == '\n') ||
               (c == ' ');
    }

    private boolean isLineValid(String lineStr){
        for(char c : lineStr.toCharArray())
            if(!isCharacterValid(c)) 
                { return false; } 
                
        if(lineStr.length() < 3)
            { return false; }
        if(!(lineStr.charAt(lineStr.length()-1) == '\n')) 
            { return false; }
        if(!(lineStr.charAt(lineStr.length()-2) == '\r')) 
            { return false; }
        if(lineStr.indexOf('\r') != lineStr.length()-2)
            { return false; }
        if(lineStr.indexOf('\n') != lineStr.length()-1)
            { return false; }
        if(lineStr.contains("  "))
            { return false; }
        if(lineStr.charAt(0) == ' ')
            { return false; }
        if(lineStr.charAt(lineStr.length()-3) == ' ')
            { return false; }                   
        
        return true;
    }

    private char getNextChar(){
        return (char) nextCharAsInt();
    }

    private int nextCharAsInt(){
        try {
            return in.read();
        } 
        catch (SocketTimeoutException e) { exit = true;}
        catch (IOException e) { e.printStackTrace(); }

        return -1;
    }
}