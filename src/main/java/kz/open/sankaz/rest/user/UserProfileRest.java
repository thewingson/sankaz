package kz.open.sankaz.rest.user;

import kz.open.sankaz.mapper.SecUserMapper;
import kz.open.sankaz.pojo.filter.ChangePasswordFilter;
import kz.open.sankaz.pojo.filter.SecUserEditFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_USER')")
@RestController
@RequestMapping("/users/profiles")
public class UserProfileRest {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    private SecUserMapper userMapper;

    @Autowired
    public UserProfileRest(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<?> getUser(HttpServletRequest request) {
        try {
            Long userId = authService.getUserId(request);
            authService.checkIfOwnProfile(userId);
            return ResponseModel.success(userMapper.userToOwnProfileDto(userService.getOne(userId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> editUser(@PathVariable("userId") Long userId,
                                                    @Valid @RequestBody SecUserEditFilter filter) {
        try {
            authService.checkIfOwnProfile(userId);
            userService.updateProfile(userId, filter);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
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

    @PutMapping("/{userId}/picture")
    public ResponseEntity<?> changePicture(@PathVariable("userId") Long userId,
                                            @Param("file") MultipartFile file) {
        try {
            authService.checkIfOwnProfile(userId);
            return ResponseModel.success(userService.changePicture(userId, file));
        } catch (Exception e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/picture")
    public ResponseEntity<?> deletePic(@PathVariable(name = "userId") Long userId) {
        try{
            authService.checkIfOwnProfile(userId);
            userService.deletePicture(userId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
