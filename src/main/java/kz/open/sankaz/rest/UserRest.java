package kz.open.sankaz.rest;

import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserRest {
    private final UserService userService;

    @Autowired
    public UserRest(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(userService.getUsers(null));
    }

    @PostMapping
    public ResponseEntity<?> addOne(@RequestBody SecUser user) {
        try {
            userService.createUser(user);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
