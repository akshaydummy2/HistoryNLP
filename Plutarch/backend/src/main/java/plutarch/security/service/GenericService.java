package plutarch.security.service;


import plutarch.security.model.User;

import java.util.List;

public interface GenericService {
    User findByUsername(String username);

    List<User> findAllUsers();
}
