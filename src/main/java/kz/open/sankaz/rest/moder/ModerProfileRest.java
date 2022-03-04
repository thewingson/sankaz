package kz.open.sankaz.rest.moder;

import kz.open.sankaz.pojo.filter.ChangePasswordFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_MODERATOR')")
@RestController
@RequestMapping("/moders/profiles")
public class ModerProfileRest {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public ModerProfileRest(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PutMapping("/{userId}/change-pass")
    public ResponseEntity<?> changePassword(@PathVariable("userId") Long userId,
                                            @Valid @RequestBody ChangePasswordFilter filter) {
        try {
            authService.checkIfOwnProfile(userId);
            userService.changePassword(userId, filter.getPassword(), filter.getConfirmPassword());
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

}
