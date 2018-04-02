package plutarch.nlp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import plutarch.nlp.model.HistoryEvent;
import plutarch.nlp.model.HistoryEventType;
import plutarch.nlp.model.Resource;
import plutarch.nlp.model.Validity;
import plutarch.security.model.User;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * Created by stephan on 20.03.16.
 */
@RepositoryRestResource(exported = false)
public interface HistoryEventRepository extends JpaRepository<HistoryEvent, Long> {
    Optional<HistoryEvent> findFirstByValidity(Validity validity);
    Optional<HistoryEvent> findHistoryEventByPhraseAndResourceAndHistoryEventType(String phrase, Resource resource, HistoryEventType eventType);
    Integer countHistoryEventsByValidityEquals(Validity validity);
    List<HistoryEvent> findHistoryEventsByResource(Resource resource); // TODO: update with check of validity
    List<HistoryEvent> findHistoryEventsByPhraseContaining(String phrase);
    @Query(value = "SELECT he.* FROM history_events he \n" +
            "Left JOIN history_events_topics het on he.id = het.history_event_id \n" +
            "left JOIN topics t on het.topic_id = t.id\n" +
            "where t.location_id is not null and he.validity = \"VALID\"\n" +
            "Group by he.id, he.phrase\n" +
            "Limit 20", nativeQuery = true)
    List<HistoryEvent> findHistoryEventsByForMap();
}
