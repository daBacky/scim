package ch.fhnw.scim.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.scim.common.business.SystemBusiness;
import ch.fhnw.scim.common.model.System;
import ch.fhnw.scim.admin.exception.ServiceExceptionHandler;

@RestController
@RequestMapping("/system")
public class SystemService extends ServiceExceptionHandler {
    @Autowired
    private SystemBusiness system;

    @GetMapping("/all")
    public List<System> getAll() {
        return system.findAll();
    }

    @GetMapping("/get")
    public System getById(	@RequestParam(name = "id", required = false) Integer id,
                              @RequestParam(name = "name", required = false) String name,
                              @RequestParam(name = "description", required = false) String description) {
        if (id != null) {
            return system.findById(id);
        }
        if(name != null){
            return system.findByName(name);
        }
        else {
            return system.findByDescription(description);
        }
    }

    @PutMapping("/save")
    public System save(@RequestBody System input) throws Exception {
        return system.save(input);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam(name = "id") int id) {
        system.delete(id);
    }

}
