package kz.open.sankaz.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.properties.MailProperties;
import kz.open.sankaz.properties.SecurityProperties;
import kz.open.sankaz.properties.SmsProperties;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.MailSender;
import kz.open.sankaz.service.SmsSender;
import kz.open.sankaz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    @Lazy
    private UserService userService;
    @Autowired
    private SmsSender smsSender;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private SmsProperties smsProperties;
    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public SecUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null
                && authentication.getPrincipal() != null
                && !authentication.getPrincipal().equals("anonymousUser")){
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userService.getUser(username);
        }
        return null;
    }

    @Override
    public void confirmAccount(String tokenId) {
        SecUser user = userService.getUserByConfirmationId(UUID.fromString(tokenId));
        user.setConfirmedTs(LocalDateTime.now());
        user.setConfirmedBy(user.getUsername());
        user.setActive(true);
        userService.updateUser(user);
//        smsSender.sendSms("+77081997727", smsProperties.getTrialNumber(), "Kalaisyn rodnoi?");
    }

    @Override
    public void registerUser(SecUser user) {
        log.info("Start of registering user {}", user.getUsername());
        userService.createUser(user);
        user.setActive(false);
        user.setConfirmedBy(null);
        user.setConfirmedTs(null);
        userService.updateUser(user);

        String message = String.format("Hello, %s!" +
                        "\nWelcome to SanKaz!" +
                        "\nTo confirm activation, please, visit next link http://localhost:8080/auth/confirm-account?tokenId=%s",
                user.getFullName(),
                user.getConfirmationId());
        mailSender.sendMail(mailProperties.getUsername(), user.getEmail(), "Activation code", message);
        log.info("End of registering user {}", user.getUsername());
    }

    @Override
    public Map<String, Object> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        response.setContentType(APPLICATION_JSON_VALUE);
        String authorizationHeader = request.getHeader(AUTHORIZATION);
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(securityProperties.getSecurityTokenSecret().getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                SecUser user = userService.getUser(username);
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .sign(algorithm);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                Map<String, String> tokens = new HashMap<>();
                result.put("access_token", accessToken);
                result.put("refresh_token", refreshToken);
        return result;
    }

    @Override
    public void signOut(String username) {
        SecUser user = userService.getUser(username);
        user.setLoggedOut(true);
        userService.updateUser(user);
    }
}
