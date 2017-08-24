package ch.fhnw.scim.common.business;

import java.util.List;

import ch.fhnw.scim.common.business.repository.SystemRepo;
import ch.fhnw.scim.common.business.repository.UserAuthorizationRepo;
import ch.fhnw.scim.common.business.repository.UserRepo;
import ch.fhnw.scim.common.helper.ValidateHelper;
import ch.fhnw.scim.common.model.System;
import ch.fhnw.scim.common.model.UserAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SystemBusiness {

    @Autowired
    private LogBusiness log;

    @Autowired
    private SystemRepo systemRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserAuthorizationRepo uaRepo;

    public List<System> findAll() {
        List<System> systems = systemRepo.findAll();
        log.info(String.format("Incoming request for all Systems. Found: %d System(s)", systems.size()));
        return systems;
    }

    public System findById(int id) {
        log.info("Incoming request for System with Id: " + id);
        return systemRepo.findById(id);
    }

    public System findByName(String name) {
        log.info("Incoming request for System with name: " + name);
        return systemRepo.findByName(name);
    }

    public System findByDescription(String description){
        log.info("Incoming request for System with description: " + description);
        return systemRepo.findByName(description);
    }

    public void delete(int id) {
        System system = findById(id);
        if (system != null) {
            List<UserAuthorization> authorizations = uaRepo.findBySystemId(system.getId());
            uaRepo.delete(authorizations);
            log.info("Delete System with Id: " + system.getId());
            systemRepo.delete(system);
        }
    }

    public System save(System system) throws Exception {
        ValidateHelper.validate(system);
        log.info("Save System with Name: " + system.getName() + system.getDescription());
        return systemRepo.save(system);
    }
}