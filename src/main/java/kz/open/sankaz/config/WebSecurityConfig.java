package kz.open.sankaz.config;

import kz.open.sankaz.filter.CustomAuthenticationFilter;
import kz.open.sankaz.filter.CustomAuthorizationFilter;
import kz.open.sankaz.properties.SecurityProperties;
import kz.open.sankaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;

    @Autowired
    public WebSecurityConfig(UserService userService,
                             PasswordEncoder passwordEncoder,
                             SecurityProperties securityProperties) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.securityProperties = securityProperties;
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
//                        "/auth/sign-up/**",
//                        "/auth/refresh-token/**",
//                        "/auth/sign-in/**",
//                        "/auth/confirm-account/**",
//                        "/auth/sign-out/**",
                        "/users/auth/**")
                .permitAll();
//        http
//                .authorizeHttpRequests()
//                .antMatchers(HttpMethod.GET, "/users/**")
//                .hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER");

        http
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated();

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(securityProperties, userService), UsernamePasswordAuthenticationFilter.class);

//        http
//                .logout()
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/sign-out"))
//                .logoutSuccessUrl("/users");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}