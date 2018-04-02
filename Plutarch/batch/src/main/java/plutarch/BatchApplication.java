package plutarch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import plutarch.batch.jobs.CreateResourcesFromTopicsJob;
import plutarch.batch.jobs.LoadResourcesFromFileJob;
import plutarch.batch.jobs.ScrapeResourcesJob;
import plutarch.batch.jobs.NLPJob;

import java.util.Date;

/**
 * Created by joshs on 2/1/2018.
 */
@ComponentScan(basePackages = {"plutarch.batch", "plutarch.nlp"})
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
public class BatchApplication {

    private static final Logger logger = LoggerFactory.getLogger(BatchApplication.class);

    private static final String SCRAPE_BATCH = "plutarch.batch.jobs.ScrapeResourcesJob";
    private static final String LOAD_RESOURCES_FILE = "plutarch.batch.jobs.LoadResourcesFromFileJob";
    private static final String CREATE_RESOURCES_FROM_TOPICS = "plutarch.batch.jobs.CreateResourcesFromTopics";

    public static void main(String[] args) throws BeansException, InterruptedException {
        SpringApplication app = new SpringApplication(BatchApplication.class);
        app.setWebEnvironment(false);

        // I hate the below approach but it works for now
        // TODO: can we make this better?
        if (args.length > 0) {
            String jobName = args[0];

            ApplicationContext context = new AnnotationConfigApplicationContext(BatchApplication.class);
            NLPJob job = null;

            if (jobName.equals(SCRAPE_BATCH)) {
                job = context.getBean(ScrapeResourcesJob.class);
            } else if (jobName.equals(LOAD_RESOURCES_FILE)) {
                job = context.getBean(LoadResourcesFromFileJob.class);
            } else if (jobName.equals(CREATE_RESOURCES_FROM_TOPICS)) {
                job = context.getBean(CreateResourcesFromTopicsJob.class);
            } else {
                logger.info("No matching jobs found for: " + jobName);
            }

            if (job != null) {
                logger.info("Starting Job: " + jobName + " @ " + new Date());

                job.Execute();

                logger.info("Finished Job: " + jobName + " @ " + new Date());
            }
        }

        System.exit(0);
    }
}
