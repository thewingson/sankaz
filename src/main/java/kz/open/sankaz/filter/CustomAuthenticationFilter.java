package kz.open.sankaz.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.pojo.dto.UsernamePasswordDto;
import kz.open.sankaz.properties.SecurityProperties;
import kz.open.sankaz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final SecurityProperties securityProperties;
    private final UserService userService;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, SecurityProperties securityProperties, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.securityProperties = securityProperties;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String jsonBody = null;
        try {
            jsonBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка в теле запроса. Пожалуйста, попробуйте еще раз");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        UsernamePasswordDto usernamePasswordDto = null;
        try {
            usernamePasswordDto = objectMapper.readValue(jsonBody, UsernamePasswordDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при чтении данных. Пожалуйста, попробуйте еще раз");
        }

        String username = usernamePasswordDto.getUsername();
        String password = usernamePasswordDto.getPassword();
        log.info("Request parameter [username]: {}", username);
        log.info("Request parameter [password]: {}", password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try{
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            return authenticate;
        } catch (AuthenticationException e){
            throw new ForbiddenException("Неправильный номер или пароль. Пожалуйста, попробуйте еще раз");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        SecUser user = (SecUser) authentication.getPrincipal();
//        user.setLoggedOut(false);
//        userService.updateUser(user);

        Algorithm algorithm = Algorithm.HMAC256(securityProperties.getSecurityTokenSecret().getBytes());
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("userId", user.getId())
                .sign(algorithm);
        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        response.setContentType(APPLICATION_JSON_VALUE);
        Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("userId", user.getId());
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    private class ForbiddenException extends RuntimeException {
        public ForbiddenException(String message) {
            super(message);
        }
    }
}
