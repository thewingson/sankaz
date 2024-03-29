package kz.open.sankaz.rest.admin;

import kz.open.sankaz.exception.MessageCodeException;
import kz.open.sankaz.mapper.SecUserMapper;
import kz.open.sankaz.pojo.filter.ResetPasswordFilter;
import kz.open.sankaz.pojo.filter.UserCreateFilter;
import kz.open.sankaz.pojo.filter.UserEditFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/users")
public class AdminUserRest {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    private SecUserMapper userMapper;

    @Autowired
    public AdminUserRest(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        try{
            return ResponseModel.success(userService.getAllPage(page, size));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/for-org")
    public ResponseEntity<?> getListForOrg(@RequestParam(value = "fullName", defaultValue = "") String fullName,
                                    @RequestParam(value = "telNumber", defaultValue = "") String telNumber,
                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "5") int size) {
        try{
            return ResponseModel.success(userMapper.userToSecUserForNewOrgDto(userService.getAllPageWithFilter(fullName, telNumber, page, size)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "userId") Long userId) {
        try{
            return ResponseModel.success(userMapper.userToDto(userService.getOne(userId)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@Valid @RequestBody UserCreateFilter filter) {
        try{
            return ResponseModel.success(userMapper.userToDto(userService.createOne(filter)));
        } catch (MessageCodeException e){
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (Exception e){
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "userId") Long userId,
                                         @Valid @RequestBody UserEditFilter filter) {
        try{
            return ResponseModel.success(userMapper.userToDto(userService.editOne(userId, filter)));
        } catch (Exception e){
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "userId") Long userId) {
        try{
            userService.deleteOneById(userId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/reset-pass")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordFilter filter) {
        try {
            return ResponseModel.success(authService.resetPassword(filter.getTelNumber(), filter.getPassword(), filter.getConfirmPassword()));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }
}
