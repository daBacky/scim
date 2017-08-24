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

import ch.fhnw.scim.common.business.UserBusiness;
import ch.fhnw.scim.common.model.User;
import ch.fhnw.scim.admin.exception.ServiceExceptionHandler;

@RestController
@RequestMapping("/user")
public class UserService extends ServiceExceptionHandler {

    @Autowired
    private UserBusiness user;

    @GetMapping("/all")
    public List<User> getAll() {
        return user.findAll();
    }

    @GetMapping("/get")
    public User getById(@RequestParam(name = "id") int id) {
        return user.findById(id);
    }

    @PutMapping("/save")
    public User save(@RequestBody User input) throws Exception {
        return user.save(input);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam(name = "id") int id) {
        user.delete(id);
    }

    @GetMapping("/login")
    public User save(	@RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String system) throws Exception {
        return user.login(email, password, system);
    }
}
