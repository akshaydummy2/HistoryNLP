package plutarch.security.repository;

import org.springframework.data.repository.CrudRepository;
import plutarch.security.model.Role;

/**
 * Created by nydiarra on 06/05/17.
 */
public interface RoleRepository extends CrudRepository<Role, Long> {
}
