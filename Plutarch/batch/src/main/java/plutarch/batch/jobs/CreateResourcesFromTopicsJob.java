package plutarch.batch.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import plutarch.BatchApplication;
import plutarch.batch.crawl.Crawler;
import plutarch.batch.crawl.CrawlerFactory;
import plutarch.nlp.model.Resource;
import plutarch.nlp.model.Topic;
import plutarch.nlp.service.DataService;

import java.util.Date;
import java.util.List;

@Component
public class CreateResourcesFromTopicsJob implements NLPJob {

    private static final Logger logger = LoggerFactory.getLogger(BatchApplication.class);

    @Autowired
    private DataService dataService;

    public void Execute() {
        // Run just one batch of topics
        List<Topic> topics = dataService.GetTopicsWithNoResource();

        for (Topic topic: topics) {
            for (Crawler crawler: CrawlerFactory.GetCrawlers()) {
                String url = crawler.Crawl(topic);
                if (url != null) {
                    // Create a new resource to save with this topic as reference
                    Resource resource = new Resource();
                    resource.setTopic(topic);
                    resource.setUrl(url);

                    dataService.AddResource(resource);

                    logger.info("Added new Resource: " + resource.getUrl() + "; Topic: " + topic.getWord());
                }

                // Update topics crawl date regardless of the result
                topic.setLastCrawlDate(new Date());
                dataService.UpdateTopicCrawlDate(topic);
            }
        }
    }
}
