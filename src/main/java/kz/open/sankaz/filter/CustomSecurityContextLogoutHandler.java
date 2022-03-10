package kz.open.sankaz.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.model.SecUserToken;
import kz.open.sankaz.properties.SecurityProperties;
import kz.open.sankaz.repo.SecUserTokenRepo;
import kz.open.sankaz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class CustomSecurityContextLogoutHandler implements LogoutSuccessHandler {
    private final SecUserTokenRepo tokenRepo;
    private final SecurityProperties securityProperties;
    private final UserService userService;

    public CustomSecurityContextLogoutHandler(SecUserTokenRepo tokenRepo, SecurityProperties securityProperties, UserService userService) {
        this.tokenRepo = tokenRepo;
        this.securityProperties = securityProperties;
        this.userService = userService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader == null || authorizationHeader.isEmpty()){
            throw new IOException("Пропущен параметр \"Authorization\" в загаловке!");
        }
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256(securityProperties.getSecurityTokenSecret().getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        SecUser user = (SecUser) userService.loadUserByUsername(username);
        SecUserToken userToken = tokenRepo.findByUserAndAccessToken(user, token);

        if(userToken == null || userToken.getIsBlocked()){
            throw new RuntimeException("Вы отправили недействительный токен");
        }
        userToken.setIsBlocked(true);
        userToken.setBlockDate(LocalDateTime.now());
        tokenRepo.save(userToken);
    }
}
