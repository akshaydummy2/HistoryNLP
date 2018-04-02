package plutarch.nlp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import plutarch.nlp.model.Resource;
import plutarch.nlp.model.Topic;

import java.util.List;
import java.util.Optional;

/**
 * Created by stephan on 20.03.16.
 */
@RepositoryRestResource(exported = false)
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> getTopicByWordAndNer(String word, String ner);
    Optional<Topic> getFirstByLocationIsNullAndNer(String ner);
    Integer countTopicsByLocationIsNullAndNer(String ner);
    @Query(value = "SELECT t.* FROM topics t WHERE (SELECT count(*) FROM resources r where r.topic_id = t.id) = 0 and t.ner = 'PERSON' and t.lastCrawlDate is null limit 20", nativeQuery = true)
    List<Topic> findTopicsWithNoResource();
}
