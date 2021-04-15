package de.jstacs.service.endpoints;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.jstacs.service.data.entities.User;
import de.jstacs.service.data.repositories.UserRepository;
import lombok.Data;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5000", "https://jstacs-online.herokuapp.com" })
@Data
@RestController
@RequestMapping("users")
public class UsersEndpoint {

    private final UserRepository userRepository;

    @GetMapping("check")
    public User checkUser(@RequestParam("user-id") String userId) {
        if (userId.isEmpty()) {
            User newUser = new User();
            return userRepository.save(newUser);
        }

        Optional<User> knownUser = userRepository.findById(userId);
        if (!knownUser.isPresent()) {
            User newUser = new User();
            return userRepository.save(newUser);
        }

        return knownUser.get();
    }

}
