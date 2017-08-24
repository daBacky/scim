package ch.fhnw.scim.common.business.repository;

import java.util.List;

import ch.fhnw.scim.common.model.UserAuthorization;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserAuthorizationRepo extends CrudRepository<UserAuthorization, Long> {

    List<UserAuthorization> findAll();

    UserAuthorization findById(int id);

    void delete(UserAuthorization entity);

    UserAuthorization save(UserAuthorization entity);

    @Query(value = "select * from user_authorization ua where user_id = :userId", nativeQuery = true)
    List<UserAuthorization> findByUserId(@Param("userId") int userId);

    @Query(value = "select * from user_authorization ua where system_id = :systemId", nativeQuery = true)
    List<UserAuthorization> findBySystemId(@Param("systemId") int systemId);
}

