package de.jstacs.service.endpoints;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.jstacs.service.data.entities.ToolExecution;
import de.jstacs.service.data.entities.User;
import de.jstacs.service.data.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5000", "https://jstacs-online.herokuapp.com" })
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UsersEndpoint {

    private final UserRepository userRepository;

    @GetMapping
    public List<ToolExecution> getAllToolExecutions(@RequestHeader("user-id") String userId) {
        Optional<User> optionalUser = this.userRepository.findById(userId);
        return optionalUser.get().getExecutions();
    }

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
