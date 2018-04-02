package plutarch.nlp.model;


import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by joshs on 6/28/2017.
 */
@Entity
@Table(name = "topics", uniqueConstraints = {@UniqueConstraint(columnNames = { "word", "ner" })})
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String word;
    private String ner;
    @OneToOne(cascade={CascadeType.ALL})
    private Location location;
    private Date lastCrawlDate;
    @OneToOne
    private Topic parentTopic;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word)
    {
        this.word = StringUtils.stripAccents(word);
    }

    public String getNer() {
        return ner;
    }

    public void setNer(String ner) {
        this.ner = ner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getLastCrawlDate() {
        return lastCrawlDate;
    }

    public void setLastCrawlDate(Date lastCrawlDate) {
        this.lastCrawlDate = lastCrawlDate;
    }

    public Topic getParentTopic() {
        return parentTopic;
    }

    public void setParentTopic(Topic parentTopic) {
        this.parentTopic = parentTopic;
    }

    public Topic() {}

    public Topic(String word, String ner) {
        setWord(word);
        setNer(ner);
    }

    public boolean equals(Object o) {
        if (o instanceof Topic) {
            Topic toCompare = (Topic)o;
            return this.getWord().toString().equals(toCompare.getWord().toString()) &&
                    this.getNer().toString().equals(toCompare.getNer().toString());
        }
        return false;
    }
}
