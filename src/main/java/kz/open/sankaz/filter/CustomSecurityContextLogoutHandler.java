package kz.open.sankaz.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kz.open.sankaz.model.JwtBlackList;
import kz.open.sankaz.properties.SecurityProperties;
import kz.open.sankaz.service.JwtBlackListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class CustomSecurityContextLogoutHandler implements LogoutSuccessHandler {
    private final JwtBlackListService jwtBlackListService;
    private final SecurityProperties securityProperties;

    public CustomSecurityContextLogoutHandler(JwtBlackListService jwtBlackListService, SecurityProperties securityProperties) {
        this.jwtBlackListService = jwtBlackListService;
        this.securityProperties = securityProperties;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("CustomSecurityContextLogoutHandler. Executing onLogoutSuccess()");
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader == null || authorizationHeader.isEmpty()){
            throw new IOException("Header param \"Authorization\" is missed!");
        }
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256(securityProperties.getSecurityTokenSecret().getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();

        log.info("CustomSecurityContextLogoutHandler. Creating JwtBlackList");
        JwtBlackList blackListItem = new JwtBlackList();
        blackListItem.setUsername(username);
        blackListItem.setAccessToken(token);
        blackListItem.setAccessToken(token);
        jwtBlackListService.addOne(blackListItem);
        log.info("CustomSecurityContextLogoutHandler. Finished creating JwtBlackList");
        log.info("CustomSecurityContextLogoutHandler. Finished executing onLogoutSuccess()");
    }
}
