package ch.fhnw.scim.common.business.repository;

import java.util.List;

import ch.fhnw.scim.common.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {

    List<User> findAll();

    User findById(int id);

    User findByScimId(String scimId);

    void delete(User entity);

    User save(User entity);

    User findByEmailAndPassword(String email, String password);
}

