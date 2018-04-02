package plutarch.batch.crawl;

import java.util.ArrayList;
import java.util.List;

public class CrawlerFactory {
    public static List<Crawler> GetCrawlers() {
        List<Crawler> crawlers = new ArrayList<>();

        crawlers.add(new WikipediaCrawler());

        return crawlers;
    }
}
