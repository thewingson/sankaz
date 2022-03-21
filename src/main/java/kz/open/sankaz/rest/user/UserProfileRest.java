package kz.open.sankaz.rest.user;

import kz.open.sankaz.mapper.SecUserMapper;
import kz.open.sankaz.pojo.filter.ChangePasswordFilter;
import kz.open.sankaz.pojo.filter.SecUserEditFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.SanService;
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
    private SanService sanService;

    @Autowired
    private SecUserMapper userMapper;

    @Autowired
    public UserProfileRest(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        try {
            Long userId = authService.getUserId(request);
            return ResponseModel.success(userMapper.userToOwnProfileDto(userService.getOne(userId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/new-pass")
    public ResponseEntity<?> setNewPasswordToCurrentUser(HttpServletRequest request,
                                            @Valid @RequestBody ChangePasswordFilter filter) {
        try {
            Long userId = authService.getUserId(request);
            return ResponseModel.success(userService.changePassword(userId, filter.getPassword(), filter.getConfirmPassword(), filter.getOldPassword()));
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

    @PutMapping("/{userId}/picture")
    public ResponseEntity<?> changePicture(HttpServletRequest request,
                                           @PathVariable("userId") Long userId, // todo: remove from mobile and here
                                            @Param("file") MultipartFile file) {
        try {
            userId = authService.getUserId(request);
            return ResponseModel.success(userService.changePicture(userId, file));
        } catch (Exception e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/picture")
    public ResponseEntity<?> deletePic(HttpServletRequest request,
                                       @PathVariable(name = "userId") Long userId) {// todo: remove from mobile and here
        try{
            userId = authService.getUserId(request);
            userService.deletePicture(userId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/notifications")
    public ResponseEntity<?> getNotifications(HttpServletRequest request,
                                              @RequestParam(value="page", defaultValue = "0") Integer page,
                                              @RequestParam(value="size", defaultValue = "20") Integer size) {
        try {
            Long userId = authService.getUserId(request);
            return ResponseModel.success(userService.getNotifications(userId, page, size));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/notifications/{notId}")
    public ResponseEntity<?> viewNotification(@PathVariable("notId") Long notId) {
        try {
            userService.viewNotification(notId);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/fav/{sanId}")
    public ResponseEntity<?> addFav(HttpServletRequest request,
                                    @PathVariable(name = "sanId") Long sanId) {
        try {
            Long userId = authService.getUserId(request);
            sanService.addFav(userId, sanId);
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/fav/list")
    public ResponseEntity<?> getFavs(HttpServletRequest request,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        try{
            Long userId = authService.getUserId(request);
            return ResponseModel.success(sanService.getFavs(userId, page, size));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
