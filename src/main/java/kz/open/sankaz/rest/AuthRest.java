package kz.open.sankaz.rest;

import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/auth")
public class AuthRest {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthRest(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SecUser secUser) {
        try {
            authService.registerUser(secUser);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<?> confirmAccount(@RequestParam("tokenId") String tokenId) {
        try {
            authService.confirmAccount(tokenId);
            return ResponseEntity.ok("Your account has been confirmed successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(@Param("username") String username) throws IOException {
        try {
            authService.signOut(username);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
