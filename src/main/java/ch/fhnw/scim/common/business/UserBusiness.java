package ch.fhnw.scim.common.business;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import ch.fhnw.scim.common.business.repository.UserAuthorizationRepo;
import ch.fhnw.scim.common.business.repository.UserRepo;
import ch.fhnw.scim.common.exception.BusinessException;
import ch.fhnw.scim.common.helper.ValidateHelper;
import ch.fhnw.scim.common.model.User;
import ch.fhnw.scim.common.model.UserAuthorization;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserBusiness {

    @Autowired
    private LogBusiness log;

    @Autowired
    private UserRepo repo;

    @Autowired
    private UserAuthorizationRepo userAuthrepo;

    public List<User> findAll() {
        List<User> users = repo.findAll();
        for (User user : users) {
            Hibernate.initialize(user.getUserAuthorizations());
        }

        log.info(String.format("Incoming request for all users. Found: %d user(s)", users.size()));
        return users;
    }

    public User findById(int id) {
        log.info("Incoming request for user with Id: " + id);
        User user = repo.findById(id);

        if (user != null) {
            Hibernate.initialize(user.getUserAuthorizations());
        }

        return user;
    }

    public User findByScimId(String scimId) {
        log.info("Incoming request for user with scimId: " + scimId);
        User user = repo.findByScimId(scimId);

        if (user != null) {
            Hibernate.initialize(user.getUserAuthorizations());
        }

        return user;
    }

    public User findByEmailAndPassword(String email, String password) {
        return repo.findByEmailAndPassword(email, password);
    }

    public void delete(int id) {
        User user = findById(id);
        if (user != null) {
            log.info("Delete user with Id: " + user.getId());
            repo.delete(user);
        }
    }

    public User save(User user) throws Exception {
        ValidateHelper.validate(user);

        if (user.getId() == 0) {
            user.setCreated(new Date());
        }

        if (StringUtils.isEmpty(user.getScimId())) {
            user.setScimId(UUID.randomUUID().toString());
        }

        user.setLastModified(new Date());
        log.info("Save user with Name: " + user.getFirstName());
        return repo.save(user);
    }

    public User login(String email, String password, String system) throws BusinessException {
        boolean authorized = false;
        User user = repo.findByEmailAndPassword(email, password);

        if(user == null) {
            throw new BusinessException("We can't find the user, or you've entered a wrong email/password combination");
        }

        if(!user.isActive()) {
            throw new BusinessException("Your user has been locked. Please contact support");
        }

        if (user.getUserAuthorizations() != null) {
            for(UserAuthorization auth : user.getUserAuthorizations()) {
                if(system.equalsIgnoreCase(auth.getSystem().getName())) {
                    authorized = true;
                    break;
                }
            }
        }

        if (authorized) {
            return user;
        }
        throw new BusinessException("User is not authorized");
    }
}