package plutarch.batch.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import plutarch.BatchApplication;
import plutarch.batch.service.NLPService;

import java.util.Date;

/**
 * Created by joshs on 6/27/2017.
 */
@Component
public class ScrapeResourcesJob implements NLPJob {

    private static final Logger logger = LoggerFactory.getLogger(BatchApplication.class);

    @Autowired
    private NLPService nlpService;

    public void Execute() {

        try {
            int resourceCounter = 0;
            boolean resourceScraped = true;
            while (resourceScraped) {
                logger.info("Started resource " + resourceCounter + " - " + new Date());
                resourceScraped = nlpService.ScrapeNext();
                logger.info("Finished resource " + resourceCounter + " - " + new Date());

                resourceCounter++;
                logger.info("Resource count: " + resourceCounter);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
