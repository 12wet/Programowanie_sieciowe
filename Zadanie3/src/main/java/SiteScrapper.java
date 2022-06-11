import com.google.gson.Gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class SiteScrapper{

    private void scrap() {
        Document doc = getDocument();
        if (doc == null)
            siteUnavailable();
        String exchangeRate = getExchangeRate(doc.body().text());
        if (exchangeRate == null)
            siteUnavailable();
        System.out.println(exchangeRate);
    }

    private Document getDocument(){
        try {
            SiteChecker sc = new SiteChecker();

            if(sc.checkSite() == 0){
                return Jsoup.parse(sc.getHTML());
            }
            return null;

        } catch (IOException e) {
            return null;
        }
    }

    private String getExchangeRate(String json){
        try {
            JsonArray jArr = new Gson().fromJson(json, JsonArray.class);
            JsonObject jObj = jArr.get(0).getAsJsonObject();
            return jObj.get("directExchangeOffers")
                    .getAsJsonObject()
                    .get("forexNow")
                    .toString();
        } catch (Exception e) {
            return null;
        }
    }

    private void siteUnavailable() {
        System.exit(1);
    }

    public static void main(String[] args) {
        SiteScrapper sc = new SiteScrapper();
        sc.scrap();
    }
}
