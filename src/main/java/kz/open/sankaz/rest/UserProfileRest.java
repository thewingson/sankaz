package kz.open.sankaz.rest;

import kz.open.sankaz.dto.ChangePasswordDto;
import kz.open.sankaz.dto.SecUserEditDto;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/users")
public class UserProfileRest {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserProfileRest(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    // ONLY FOR ADMIN
    @GetMapping("/profiles")
    public ResponseEntity<?> getUsers() {
        try {
            return ResponseModel.success(userService.getAllDto());
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/profiles/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") Long userId) {
        try {
            return ResponseModel.success(userService.getOneDto(userId));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/profiles/{userId}")
    public ResponseEntity<?> editUser(@PathVariable("userId") Long userId,
                                                    @RequestBody SecUserEditDto userEditDto) {
        try {
            userService.updateOneDto(userId, userEditDto);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/profiles/{userId}/change-pass")
    public ResponseEntity<?> changePassword(@PathVariable("userId") Long userId,
                                      @RequestBody ChangePasswordDto passwordDto) {
        try {
            userService.changePassword(userId, passwordDto);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/profiles/{userId}/picture")
    public ResponseEntity<?> changePicture(@PathVariable("userId") Long userId,
                                            @Param("file") MultipartFile file) {
        try {
            return ResponseModel.success(userService.changePicture(userId, file));
        } catch (Exception e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/profiles/{userId}/picture")
    public ResponseEntity<?> deletePic(@PathVariable(name = "userId") Long userId) {
        try{
            userService.deletePicture(userId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
