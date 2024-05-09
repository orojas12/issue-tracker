package dev.oscarrojas.issuetracker.user;

import dev.oscarrojas.issuetracker.exceptions.DuplicateElementException;
import dev.oscarrojas.issuetracker.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserManager userManager;

    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userManager.getAllUsers();
    }

    @PostMapping
    public UserDto createUser(@RequestBody CreateUserRequest createUserRequest) {
        try {
            return userManager.createUser(createUserRequest);
        } catch (DuplicateElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/{username}")
    public UserDto getUser(@PathVariable("username") String username) throws NotFoundException {
        Optional<UserDto> user = userManager.getUser(username);
        return user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User '%s' not found".formatted(username)));
    }
}
