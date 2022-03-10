package kz.open.sankaz.rest.user;

import kz.open.sankaz.exception.NumberConfirmationException;
import kz.open.sankaz.pojo.filter.FinishRegFilter;
import kz.open.sankaz.pojo.filter.RegisterFilter;
import kz.open.sankaz.pojo.filter.ResetPasswordFilter;
import kz.open.sankaz.pojo.filter.TelNumberFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@PreAuthorize("hasRole('ROLE_USER')")
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

    @PreAuthorize("permitAll()")
    @PostMapping(value = "/numbers/is-free")
    public ResponseEntity<?> isNumberFree(@Valid @RequestBody TelNumberFilter filter) {
        try {
            return ResponseModel.success(authService.isNumberFree(filter.getTelNumber()));
        } catch (RuntimeException e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/send-conf")
    public ResponseEntity<?> sendConfirmationNumber(@Valid @RequestBody TelNumberFilter filter) {
        try {
            authService.sendConfirmationNumber(filter.getTelNumber());
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/check-conf")
    public ResponseEntity<?> checkConfirmationNumber(@Valid @RequestBody RegisterFilter filter) {
        try {
            authService.checkConfirmationNumber(filter);
            return ResponseModel.successPure();
        } catch (NumberConfirmationException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getReason());
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/finish-reg")
    public ResponseEntity<?> finishRegistration(@Valid @RequestBody FinishRegFilter filter) {
        try {
            return ResponseModel.success(authService.finishRegistration(filter));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/send-reset")
    public ResponseEntity<?> sendResetNumber(@Valid @RequestBody TelNumberFilter filter) {
        try {
            authService.sendResetNumber(filter.getTelNumber());
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/check-reset")
    public ResponseEntity<?> checkResetNumber(@Valid @RequestBody RegisterFilter registerFilter) {
        try {
            authService.checkResetNumber(registerFilter.getTelNumber(), registerFilter.getResetNumber());
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/reset-pass")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordFilter filter) {
        try {
            return ResponseModel.success(authService.resetPassword(filter.getTelNumber(), filter.getPassword(), filter.getConfirmPassword()));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
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

}
