public class Line {
    
    private final String line;
    private final boolean errFlag;

    public Line(String line, boolean errFlag){
        this.line = line;
        this.errFlag = errFlag;
    }

    public String getLine(){
        return line;
    }

    public boolean getErrFlag(){
        return errFlag;
    }
}