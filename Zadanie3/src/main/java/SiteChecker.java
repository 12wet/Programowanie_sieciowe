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

    private String html;

    SiteChecker() throws IOException{
        site = new URL("https://klient.internetowykantor.pl/api/public/marketBrief");
        httpCon = (HttpURLConnection) site.openConnection();
        in = new BufferedReader(new InputStreamReader(
                httpCon.getInputStream()));
        addHook();
    }

    public int checkSite(){
        try {
            html = getHtmlBody();
            if(!isHTMLorJSON() || !isCodeCorrect())
                return 1;
            if(containsPattern())
                return 0;
            else return 1;
        } catch (IOException e) {
            return 1;
        }
    }

    private String getHtmlBody() throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException ignored) {
        }
        return contentBuilder.toString();
    }

    private boolean containsPattern()
            throws IOException{

        return html.contains(SiteChecker.SITE_PATTERN);
    }

    private boolean isHTMLorJSON(){
        return httpCon.getContentType().equals("application/json; charset=utf-8");
    }

    private boolean isCodeCorrect() throws IOException{
        return httpCon.getResponseCode() == 200;
    }

    private void addHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(this::stopConnection));
    }

    public String getHTML(){
        return html;
    }

    private void stopConnection(){
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}