package kz.open.sankaz.config;

import kz.open.sankaz.filter.CustomAuthenticationFilter;
import kz.open.sankaz.filter.CustomAuthorizationFilter;
import kz.open.sankaz.filter.CustomSecurityContextLogoutHandler;
import kz.open.sankaz.properties.SecurityProperties;
import kz.open.sankaz.repo.SecUserTokenRepo;
import kz.open.sankaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;
    private final SecUserTokenRepo tokenRepo;

    @Autowired
    public WebSecurityConfig(UserService userService,
                             PasswordEncoder passwordEncoder,
                             SecurityProperties securityProperties,
                             SecUserTokenRepo tokenRepo) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.securityProperties = securityProperties;
        this.tokenRepo = tokenRepo;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), securityProperties, userService, tokenRepo);
        customAuthenticationFilter.setFilterProcessesUrl("/auth/sign-in");

        http.csrf().disable();
        http.cors();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().antMatchers(
                "/push/**",
                "/patient/**",
                "/dict/**",
                "/users/auth/numbers/is-free",
                "/users/books/{bookId}/payment-status",
                "/users/auth/send-conf",
                "/users/auth/check-conf",
                "/users/auth/finish-reg",
                "/users/auth/send-reset",
                "/users/auth/check-reset",
                "/users/auth/reset-pass",
                "/moders/auth/numbers/is-free",
                "/moders/auth/send-conf",
                "/moders/auth/check-conf",
                "/moders/auth/send-reset",
                "/moders/auth/check-reset",
                "/moders/auth/reset-pass",
                "/moders/books/{bookId}/pay",
                "/faqs/**"
        ).permitAll();

        http.authorizeHttpRequests().antMatchers(HttpMethod.GET, "/users/sans/**").permitAll();
        http.authorizeHttpRequests().antMatchers(HttpMethod.POST, "/users/sans").permitAll();

        http
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated();

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(securityProperties, userService, tokenRepo), UsernamePasswordAuthenticationFilter.class);

        http
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/sign-out"))
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/categories")
                .logoutSuccessHandler(new CustomSecurityContextLogoutHandler(tokenRepo, securityProperties, userService));
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        CorsFilter filter = new CorsFilter(source);
        return filter;
    }
}