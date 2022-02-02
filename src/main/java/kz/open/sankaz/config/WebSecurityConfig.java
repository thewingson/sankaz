package kz.open.sankaz.config;

import kz.open.sankaz.filter.CustomAuthenticationFilter;
import kz.open.sankaz.filter.CustomAuthorizationFilter;
import kz.open.sankaz.filter.CustomSecurityContextLogoutHandler;
import kz.open.sankaz.properties.SecurityProperties;
import kz.open.sankaz.service.JwtBlackListService;
import kz.open.sankaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;
    private final JwtBlackListService jwtBlackListService;

    @Autowired
    public WebSecurityConfig(UserService userService,
                             PasswordEncoder passwordEncoder,
                             SecurityProperties securityProperties,
                             JwtBlackListService jwtBlackListService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.securityProperties = securityProperties;
        this.jwtBlackListService = jwtBlackListService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), securityProperties, userService);
        customAuthenticationFilter.setFilterProcessesUrl("/auth/sign-in");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .authorizeHttpRequests()
                .antMatchers(
                        "/orgs/auth/**",
                        "/users/auth/**",
                        "/users/auth/sign-out")
                .permitAll();

        http
                .authorizeHttpRequests()
                .antMatchers("/orgs/auth/register-org")
                .authenticated();

        http
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated();

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(securityProperties, userService, jwtBlackListService), UsernamePasswordAuthenticationFilter.class);

        http
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/users/auth/sign-out"))
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/categories")
                .logoutSuccessHandler(new CustomSecurityContextLogoutHandler(jwtBlackListService, securityProperties));

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}