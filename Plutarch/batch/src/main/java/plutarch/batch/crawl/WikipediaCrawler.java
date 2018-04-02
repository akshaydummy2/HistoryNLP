package plutarch.batch.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import plutarch.batch.scrape.Scraper;
import plutarch.batch.scrape.ScraperFactory;
import plutarch.batch.service.TokenService;
import plutarch.nlp.model.HistoryEvent;
import plutarch.nlp.model.HistoryEventType;
import plutarch.nlp.model.Resource;
import plutarch.nlp.model.Topic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshs on 8/25/2017.
 */
public class WikipediaCrawler extends Crawler {

    public String Crawl(Topic topic) {
        try {
            String word = topic.getWord();
            word = word.replace(" ", "_");

            String url = "https://en.wikipedia.org/wiki/" + word;

            Document doc = Jsoup.connect(url).get();

            boolean personURLFound = findPersonUrl(doc);

            if (personURLFound)
                return url;

            return null;
        } catch (IOException e) {

            return null;
        }
    }

    private boolean findPersonUrl(Document doc) {
        // See if there is an infobox, which indicates this is for a person
        Elements trArray = doc.select("table.infobox tr");

        if (trArray.hasText())
            return true;
        else
            return false;
    }
}
