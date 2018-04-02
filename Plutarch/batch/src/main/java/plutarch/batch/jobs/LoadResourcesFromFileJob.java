package plutarch.batch.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import plutarch.BatchApplication;
import plutarch.nlp.model.Resource;
import plutarch.nlp.model.Topic;
import plutarch.nlp.service.DataService;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by joshs on 6/27/2017.
 */
@Component
public class LoadResourcesFromFileJob implements NLPJob {

    private static final Logger logger = LoggerFactory.getLogger(BatchApplication.class);

    @Autowired
    private DataService dataService;

    public void Execute() {

        // Load the csv file
        String csvFile = "resources_backup_jan2018.csv";

        ClassLoader classLoader = getClass().getClassLoader();
        String filename = classLoader.getResource(csvFile).getFile();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] row = line.split(cvsSplitBy);

                Topic topic = new Topic();
                topic.setNer(row[0]);
                topic.setWord(row[1]);

                // Add resources to db
                Resource resource = new Resource();
                resource.setUrl(row[2]);
                resource.setTopic(topic);

                dataService.AddResource(resource);
            }

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }
}
