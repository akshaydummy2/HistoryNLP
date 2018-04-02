package plutarch.batch.scrape;

/**
 * Created by joshs on 8/25/2017.
 */
public class ScraperFactory {
    public static Scraper Resolve(String url) {
        // TODO: move to config file
        // TODO: don't create a new object each time, move to a static list of Scraper
        if (url.contains("wikipedia.org"))
            return new WikipediaScraper();

        return null;
    }
}
