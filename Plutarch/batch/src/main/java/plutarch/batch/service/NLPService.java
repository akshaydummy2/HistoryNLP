package plutarch.batch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import plutarch.PlutarchApplication;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import plutarch.batch.scrape.Scraper;
import plutarch.batch.scrape.ScraperFactory;
import plutarch.nlp.model.*;
import plutarch.nlp.service.DataService;

import java.io.IOException;
import java.util.*;

/**
 * Created by joshs on 6/24/2017.
 */
@Service
public class NLPService {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private DataService dataService;

    private static final Logger logger = LoggerFactory.getLogger(PlutarchApplication.class);

    public NLPService() {
    }

    @Transactional
    public boolean ScrapeNext() throws Exception {
        Resource resource = dataService.GetNextResource();

        // If no resource returned, return false to indicate no resources process
        if (resource == null)
            return false;

        // Clear HistoryEvents for the resource
        boolean clearHistoryEventsResult = dataService.ClearHistoryEvents(resource);
        if (!clearHistoryEventsResult)
            logger.error("Clearing HistoryEvents has failed; url: " + resource.getUrl());

        // Process the successfully loaded resource
        List<HistoryEvent> events = Scrape(resource);

        // Save events to the DB
        boolean saveHistoryEventsResult = dataService.SaveHistoryEvents(events);
        if (!saveHistoryEventsResult) {
            logger.error("Failed to save events; count: " + events.size());

            // Stop processing
            return false;
        }

        // Mark resource as processed
        boolean processedResourceResult =  dataService.ProcessedResource(resource);
        if (!processedResourceResult)
            throw new Exception("Failed to mark resource as processed; url: " + resource.getUrl());

        logger.info("Processed resource successfully; url: " + resource.getUrl());
        return true;
    }

    public List<HistoryEvent> Scrape(Resource resource) {
        // Send in all HistoryEventTypes to scrape
        return Scrape(resource, Arrays.asList(HistoryEventType.values()));
    }

    public List<HistoryEvent> Scrape(Resource resource, List<HistoryEventType> historyEventTypes) {
        tokenService.Initialize(historyEventTypes);

        try {
            Document doc = Jsoup.connect(resource.getUrl()).get();
            List<HistoryEvent> events = new ArrayList<HistoryEvent>();

            // Resolve to domain specific scraping (ex: look for a side bar of info on wikipedia)
            Scraper scraper = ScraperFactory.Resolve(resource.getUrl());
            if (scraper != null) {
                events.addAll(scraper.Scrape(tokenService, resource, doc, historyEventTypes));
            } else {
                // log warning that url didn't have a Scraper mapped
                logger.error("No scrapper mapped for: " + resource.getUrl());
            }
            // TODO: add a generic scrapper for any random website?

            return events;
        } catch (IOException e) {
            logger.error(e.toString());

            return new ArrayList<HistoryEvent>();
        }
    }
}
