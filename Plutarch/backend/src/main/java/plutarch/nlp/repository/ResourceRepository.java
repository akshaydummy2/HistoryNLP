package plutarch.nlp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import plutarch.nlp.model.HistoryEvent;
import plutarch.nlp.model.Resource;

import java.util.Optional;

/**
 * Created by stephan on 20.03.16.
 */
@RepositoryRestResource(exported = false)
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Optional<Resource> findFirstByProcessDateIsNull();
    Optional<Resource> findResourceByUrl(String url);
    Integer countResourcesByProcessDateIsNull();
}
