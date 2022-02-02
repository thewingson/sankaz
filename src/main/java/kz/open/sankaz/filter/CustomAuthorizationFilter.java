package kz.open.sankaz.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.open.sankaz.model.JwtBlackList;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.properties.SecurityProperties;
import kz.open.sankaz.service.JwtBlackListService;
import kz.open.sankaz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;
    private final UserService userService;
    private final JwtBlackListService jwtBlackListService;

    public CustomAuthorizationFilter(SecurityProperties securityProperties, UserService userService, JwtBlackListService jwtBlackListService) {
        this.securityProperties = securityProperties;
        this.userService = userService;
        this.jwtBlackListService = jwtBlackListService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(request.getServletPath().equals("/auth/sign-in")
                || request.getServletPath().equals("/auth/sign-up")
                || request.getServletPath().equals("/auth/refresh-token")
                || request.getServletPath().equals("/auth/confirm-account")){
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256(securityProperties.getSecurityTokenSecret().getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();

                    SecUser user = (SecUser) userService.loadUserByUsername(username);
                    List<JwtBlackList> jwtBlackList = jwtBlackListService.getAllByUsername(username);
                    if(jwtBlackList.stream().map(JwtBlackList::getAccessToken).anyMatch(s -> s.equals(token))){
                        throw new Exception("Вы вышли из системы. Пожалуйста, войдите еще раз!");
                    }

                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<GrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e){
                    log.error("Error logging in {}", e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
