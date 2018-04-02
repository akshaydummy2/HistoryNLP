package plutarch.nlp.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plutarch.PlutarchApplication;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

/**
 * Created by joshs on 6/25/2017.
 */
@Entity
@Table(name = "history_events")
public class HistoryEvent {

    private static final Logger logger = LoggerFactory.getLogger(PlutarchApplication.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phrase;
    /**
     * Resource that caused this history event to be created
     */
    @OneToOne
    private Resource resource;
    /**
     * Topics are being eagerly loaded here because
     * they are a fairly small collection of items for this example.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade={PERSIST, MERGE}) // TODO: only cascade to the join table and not the topic table
    @JoinTable(name = "history_events_topics",
            joinColumns = @JoinColumn(name = "history_event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id", referencedColumnName = "id"))
    private List<Topic> topics;
    @Embedded
    private HistoryDate hDate;
    @Enumerated(EnumType.STRING)
    private HistoryEventType historyEventType;
    @Enumerated(EnumType.STRING)
    private Validity validity = Validity.Unknown;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public HistoryDate gethDate() {
        return hDate;
    }

    public void sethDate(HistoryDate hDate) {
        this.hDate = hDate;
    }

    public HistoryEventType getHistoryEventType() {
        return historyEventType;
    }

    public void setHistoryEventType(HistoryEventType historyEventType) {
        this.historyEventType = historyEventType;
    }

    public Validity getValidity() {
        return validity;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }

    public HistoryEvent(){
        topics = new ArrayList<>();
    }

    /*public int GetScore() {
        int score = getTopics().size(); // TODO: handle this better: 1 pt for each type?

        if (gethDate() != null) {
            score += 1;

            if (gethDate().getDateType().equals(DateType.Full)) {
                score += 1;
            }
        }

        return score;
    }*/

    public void AddPerson(String name) {
        addTopic(name, "PERSON");
    }

    public void AddLocation(String name) {
        addTopic(name, "LOCATION");
    }

    public void AddLocations(List<String> locations) {
        for (String location: locations) {
            AddLocation(location);
        }
    }

    public void AddOrganization(String name) {
        addTopic(name, "ORGANIZATION");
    }

    private void addTopic(String name, String ner) {
        Topic topic = getTopic(name, ner);

        if (topic == null) {
            getTopics().add(new Topic(name, ner));
        }
    }

    private Topic getTopic(String word, String ner) {
        Topic[] topics = getTopics().stream().filter(t -> t.getWord().equals(word) && t.getNer().equals(ner)).toArray(Topic[]::new);

        // ensure there is only 1 topic that matches the word and ner
        if (topics.length > 1) {
            logger.error("More than one topic on event with: { word = " + word + ", ner = " + ner);
        } else if (topics.length == 1) {
            return topics[0];
        }

        return null;
    }

    public void ReplaceTopics(List<Topic> topics) {
        for (Topic topic: topics) {
            Topic existingTopic = getTopic(topic.getWord(), topic.getNer());

            // Remove topic from event
            if (existingTopic != null) {
                getTopics().remove(existingTopic);
            }
        }

        // Add new topics as replacements
        for (Topic topic: topics) {
            // Ensure we don't add the same topics more than once
            if (!getTopics().contains(topic)) {
                getTopics().add(topic);
            }
        }
    }
}
