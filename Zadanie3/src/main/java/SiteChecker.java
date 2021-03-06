import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SiteChecker{
    private final static String SITE_PATTERN = "EUR_PLN";

    private final URL site;
    private final HttpURLConnection httpCon;
    private final BufferedReader in;

    private String siteContent;

    SiteChecker() throws IOException{
        site = new URL("https://klient.internetowykantor.pl/api/public/marketBrief");
        httpCon = (HttpURLConnection) site.openConnection();
        in = new BufferedReader(new InputStreamReader(
                httpCon.getInputStream()));
        addHook();
    }

    public int checkSite(){
        try {
            siteContent = retrieveSiteContent();
            if(!isJsonType() || !isCodeCorrect())
                return 1;
            if(containsPattern())
                return 0;
            else return 1;
        } catch (IOException e) {
            return 1;
        }
    }

    private String retrieveSiteContent() throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            String line;
            while ((line = in.readLine()) != null) {
                contentBuilder.append(line);
            }
            in.close();
        } catch (IOException ignored) {
        }
        return contentBuilder.toString();
    }

    private boolean containsPattern()
            throws IOException{

        return siteContent.contains(SiteChecker.SITE_PATTERN);
    }

    private boolean isJsonType(){
        return httpCon.getContentType().equals("application/json; charset=utf-8");
    }

    private boolean isCodeCorrect() throws IOException{
        return httpCon.getResponseCode() == 200;
    }

    private void addHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(this::stopConnection));
    }

    public String getSiteContent(){
        return siteContent;
    }

    private void stopConnection(){
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}