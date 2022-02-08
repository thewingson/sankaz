package kz.open.sankaz.rest;

import kz.open.sankaz.pojo.dto.FinishRegDto;
import kz.open.sankaz.pojo.dto.NumberDto;
import kz.open.sankaz.pojo.dto.RegisterDto;
import kz.open.sankaz.pojo.dto.ResetPasswordDto;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/users/auth")
public class UserAuthRest {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserAuthRest(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/numbers")
    public ResponseEntity<?> getNumbers() {
        try {
            return ResponseModel.success(authService.getNumbers());
        } catch (RuntimeException e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/numbers/is-free")
    public ResponseEntity<?> isNumberFree(@RequestBody NumberDto numberDto) {
        try {
            return ResponseModel.success(authService.isNumberFree(numberDto.getTelNumber()));
        } catch (RuntimeException e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/send-conf")
    public ResponseEntity<?> sendConfirmationNumber(@RequestBody NumberDto numberDto) {
        try {
            authService.sendConfirmationNumber(numberDto);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/check-conf")
    public ResponseEntity<?> checkConfirmationNumber(@RequestBody RegisterDto registerDto) {
        try {
            authService.checkConfirmationNumber(registerDto);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/finish-reg")
    public ResponseEntity<?> finishRegistration(@RequestBody FinishRegDto finishRegDto) {
        try {
            return ResponseModel.success(authService.finishRegistration(finishRegDto));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/send-reset")
    public ResponseEntity<?> sedResetNumber(@RequestBody NumberDto numberDto) {
        try {
            authService.sendResetNumber(numberDto);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/check-reset")
    public ResponseEntity<?> checkResetNumber(@RequestBody RegisterDto registerDto) {
        try {
            authService.checkResetNumber(registerDto);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/reset-pass")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        try {
            return ResponseModel.success(authService.resetPassword(resetPasswordDto));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                return ResponseEntity.ok(authService.refreshToken(request, response));
            } catch (Exception e) {
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(FORBIDDEN.value());
                response.setHeader("error", e.getMessage());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                return ResponseEntity.badRequest().body(error);
            }
        } else {
            return ResponseEntity.badRequest().body("Refresh token is missing");
        }
    }

//    @PostMapping("/sign-out")
//    public ResponseEntity<?> signOut(HttpServletRequest request) {
//        try {
//            authService.signOut(request);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

}
