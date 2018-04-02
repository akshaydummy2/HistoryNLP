package plutarch.nlp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import plutarch.PlutarchApplication;
import plutarch.nlp.model.HistoryEvent;
import plutarch.nlp.model.Resource;
import plutarch.nlp.model.Topic;
import plutarch.nlp.model.Validity;
import plutarch.nlp.repository.HistoryEventRepository;
import plutarch.nlp.repository.ResourceRepository;
import plutarch.nlp.repository.TopicRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by joshs on 1/27/2018.
 */
@Service
@Configurable
public class DataService implements IDataService {

    private static final String LOCATION = "LOCATION";

    @Autowired
    private HistoryEventRepository historyEventRepository;
    
    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private TopicRepository topicRepository;

    private static final Logger logger = LoggerFactory.getLogger(PlutarchApplication.class);

    public DataService() {

    }

    @Override
    public Resource GetNextResource() {
        Optional<Resource> resource = resourceRepository.findFirstByProcessDateIsNull();

        if (resource.isPresent())
            return resource.get();
        else
            return null;
    }

    @Override
    public void AddResource(Resource resource) {

        boolean resourceExists = CheckResourceExists(resource.getUrl());
        if (!resourceExists) {
            Resource savedResource = resourceRepository.save(resource);
            if (savedResource.getId() == 0)
                logger.error("Failed to add resource; url: " + resource.getUrl());
        }
        //TODO: make this more explicit with better error handling
    }

    public boolean CheckResourceExists(String url) {
        Optional<Resource> resource = resourceRepository.findResourceByUrl(url);
        return resource.isPresent();
    }

    public Integer GetUnprocessedResourceCount() {
        Integer count = resourceRepository.countResourcesByProcessDateIsNull();
        return count;
    }

    public HistoryEvent GetHistoryEvent(Long id) {
        HistoryEvent hEvent = historyEventRepository.findOne(id);
        return hEvent;
    }

    public HistoryEvent GetNextUnknownEvent() {
        Optional<HistoryEvent> hEvent = historyEventRepository.findFirstByValidity(Validity.Unknown);
        if (!hEvent.isPresent()) {
            logger.error("No unknown event to process");
            return null;
        }

        // Return the object
        return hEvent.get();
    }

    public Integer GetUnknownEventsCount() {
        return historyEventRepository.countHistoryEventsByValidityEquals(Validity.Unknown);
    }

    public boolean SaveHistoryEvents(List<HistoryEvent> events) {
        try {
            // Get the an existing topic if it already exists
            for (HistoryEvent event: events) {
                // Check to see in a duplicate event already exists and skip if one is found
                Optional<HistoryEvent> dupHEvent = historyEventRepository.findHistoryEventByPhraseAndResourceAndHistoryEventType(event.getPhrase(), event.getResource(), event.getHistoryEventType());
                if (dupHEvent.isPresent())
                    continue;

                // Gather all topics in the db that match
                List<Topic> dbTopics = new ArrayList<>();
                for (Topic topic: event.getTopics()) {
                    Topic dbTopic = this.getTopic(topic.getWord(), topic.getNer());
                    if (dbTopic != null) {
                        dbTopics.add(dbTopic);
                    }
                }

                // Update event topics with db topics
                event.ReplaceTopics(dbTopics);

                // Perform the save or update
                HistoryEvent historyEvent = historyEventRepository.save(event);

                // Ensure the event now has an id
                if (historyEvent.getId() == 0)
                    logger.error("HistoryEvent saved but didn't come back with a new Id");
            }

            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    public void UpdateEventValidity(HistoryEvent hEvent) {
        HistoryEvent dbHEvent = historyEventRepository.getOne(hEvent.getId());
        dbHEvent.setValidity(hEvent.getValidity());

        logger.info("Updating HistoryEvent with id: " + dbHEvent.toString() + " validity: " + dbHEvent.getValidity().name());
        historyEventRepository.save(dbHEvent);
    }

    public boolean ClearHistoryEvents(Resource resource) {
        List<HistoryEvent> historyEvents = historyEventRepository.findHistoryEventsByResource(resource);

        // Clear all topics from events we will be deleting
        for (HistoryEvent hEvent: historyEvents) {
            // Clear all events except Valid events
            if (hEvent.getValidity() != Validity.Valid) {
                hEvent.getTopics().clear();

                // Save the change to remove the relationship with topics
                historyEventRepository.save(hEvent);

                // Delete the events
                historyEventRepository.delete(hEvent);
            }
        }

        return true;
    }

    public List<HistoryEvent> SearchHistoryEvents(String searchCriteria) {
        List<HistoryEvent> historyEvents = historyEventRepository.findHistoryEventsByPhraseContaining(searchCriteria);
        return historyEvents;
    }

    public List<HistoryEvent> GetHistoryEventsMap() {
        List<HistoryEvent> events = historyEventRepository.findHistoryEventsByForMap();
        return events;
    }

    public boolean ProcessedResource(Resource resource) {
        resource.setProcessDate(new Date());
        resourceRepository.save(resource);

        return true;
    }

    public Topic GetNextEmptyTopicLocation() {
        Optional<Topic> topic = topicRepository.getFirstByLocationIsNullAndNer(LOCATION);

        if (topic.isPresent())
            return topic.get();
        else
            return null;
    }

    public void UpdateTopicLocation(Topic topic) {
        Topic dbTopic = topicRepository.getOne(topic.getId());

        // Verify that the topic has a null location and is a LOCATION topic
        if (dbTopic.getLocation() != null || !dbTopic.getNer().equals(LOCATION))
            return;

        dbTopic.setLocation(topic.getLocation());
        topicRepository.save(dbTopic);
    }

    public Integer GetEmptyLocationCount() {
        Integer count = topicRepository.countTopicsByLocationIsNullAndNer("LOCATION");
        return count;
    }

    public List<Topic> GetTopicsWithNoResource() {
        List<Topic> topics = topicRepository.findTopicsWithNoResource();
        return topics;
    }

    public void UpdateTopicCrawlDate(Topic topic) {
        Topic dbTopic = topicRepository.findOne(topic.getId());

        // Update last crawl date
        dbTopic.setLastCrawlDate(topic.getLastCrawlDate());

        topicRepository.save(dbTopic);
    }

    private Topic getTopic(String word, String ner) {
        Optional<Topic> topic = topicRepository.getTopicByWordAndNer(word, ner);
        if (!topic.isPresent())
            return null;
        else if (topic.get().getParentTopic() != null) {
            Topic parentTopic = topic.get().getParentTopic();
            while (parentTopic.getParentTopic() != null) {
                parentTopic = parentTopic.getParentTopic();
            }

            return parentTopic;
        }

        return topic.get();
    }
}
