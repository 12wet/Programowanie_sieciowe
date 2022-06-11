import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SiteChecker{
    private final static String SITE_PATTERN = "Institute of Theoretical Physics";

    private final URL site;
    private final HttpURLConnection httpCon;
    private final BufferedReader in;

    private SiteChecker() throws IOException{
        site = new URL("http://th.if.uj.edu.pl/");
        httpCon = (HttpURLConnection) site.openConnection();
        in = new BufferedReader(new InputStreamReader(
                                    httpCon.getInputStream()));
        addHook();
    }

    private boolean containsPattern()
                                        throws IOException{
        String inputLine;
        while ((inputLine = in.readLine()) != null) 
            if(inputLine.contains(SiteChecker.SITE_PATTERN)) return true;
        return false;
    }

    private boolean isHTML(){
        return httpCon.getContentType().equals("text/html");
    }

    private boolean isCodeCorrect() throws IOException{
        return httpCon.getResponseCode() == 200;
    }

    private static void exitSuccess(){
        System.exit(0);
    }

    private static void exitFailure(){
        System.exit(1);
    }

    private void addHook(){
        Runtime.getRuntime()
                .addShutdownHook(new Thread(this::stopConnection));
    }

    private void stopConnection(){
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            SiteChecker sc = new SiteChecker();
            if(!sc.isHTML() || !sc.isCodeCorrect())
                exitFailure();
            if(sc.containsPattern())
                exitSuccess();
            else exitFailure();
        } catch (IOException e) {
            exitFailure();
        }
    }
}