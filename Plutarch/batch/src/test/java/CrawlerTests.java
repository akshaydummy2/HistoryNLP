import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import plutarch.batch.crawl.Crawler;
import plutarch.batch.crawl.CrawlerFactory;
import plutarch.batch.service.TokenService;
import plutarch.nlp.model.HistoryEvent;
import plutarch.nlp.model.HistoryEventType;
import plutarch.nlp.model.Resource;
import plutarch.nlp.model.Topic;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshs on 6/25/2017.
 */

@ContextConfiguration(classes = {TestConfig.class})
public class CrawlerTests extends TestConfig{

    public CrawlerTests() {
    }

    @Test
    public void evaluatePersonURL1() {
        List<String> urls = crawlTopic("Dennis Kucinich");

        Assert.assertEquals(urls.size(), 1);
    }

    @Test
    public void evaluatePersonURL2() {
        List<String> urls = crawlTopic("Archelaus");

        Assert.assertEquals(urls.size(), 0);
    }

    @Test
    public void evaluatePersonURL3() {
        List<String> urls = crawlTopic("Philip II");

        Assert.assertEquals(urls.size(), 0);
    }

    @Test
    public void evaluatePersonURL4() {
        List<String> urls = crawlTopic("Robin");

        Assert.assertEquals(urls.size(), 0);
    }

    @Test
    public void evaluatePersonURL5() {
        List<String> urls = crawlTopic("Alexander IV");

        Assert.assertEquals(urls.size(), 0);
    }

    @Test
    public void evaluatePersonURL6() {
        List<String> urls = crawlTopic("Alexander_IV_of_Macedon");

        Assert.assertEquals(urls.size(), 1);
    }


    private List<String> crawlTopic(String personName) {
        List<String> urls = new ArrayList<>();

        Topic topic = new Topic();
        topic.setWord(personName);

        for (Crawler crawler: CrawlerFactory.GetCrawlers()) {
            String url = crawler.Crawl(topic);
            if (url != null)
                urls.add(url);
        }

        return urls;
    }
}
