package kz.open.sankaz.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kz.open.sankaz.dto.*;
import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.mapper.SecUserMapper;
import kz.open.sankaz.model.SecRole;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.properties.MailProperties;
import kz.open.sankaz.properties.SecurityProperties;
import kz.open.sankaz.properties.SmsProperties;
import kz.open.sankaz.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private RoleService roleService;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecUserMapper userMapper;

    @Value("${security.account.confirm.link}")
    private String ACCOUNT_CONFIRM_LINK;

    private static final String ACCOUNT_CONFIRM_MESSAGE = "\nWelcome to SanKaz!\nTo confirm activation, please, visit next link";
    private static final int CONFIRMATION_TIME_IN_MINUTES = 5;

    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ( authentication == null || (authentication instanceof AnonymousAuthenticationToken)) {
            return "anonymousUser";
        } else {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
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
                SecUser user = (SecUser) userService.loadUserByUsername(username);
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

//    @Override
//    public void signOut(String username) {
//        SecUser user = userService.getUser(username);
//        user.setLoggedOut(true);
//        userService.updateUser(user);
//    }

    @Override
    public List<String> getNumbers() {
        Map<String, Object> params = new HashMap<>();
        params.put("deleted", true);
        userService.getAll(params);
        return userService.getAll(params)
                .stream()
                .map(SecUser::getTelNumber)
                .collect(Collectors.toList());
    }

    @Override
    public int sendConfirmationNumber(NumberDto numberDto) {
         log.info("Start of sending confirmation number {}", numberDto.getTelNumber());
        SecUser userByNumber;
        try{
            log.info("Checking tel number in DB");
            userByNumber = userService.getUserByTelNumber(numberDto.getTelNumber());
            if(userByNumber.getConfirmedBy() != null){
                throw new RuntimeException("Number is already taken by other user! Please use other number.");
            }
        } catch (EntityNotFoundException e){
            // FREE NUMBER
            log.info("Number is free");
            userByNumber = new SecUser();
        }

        userByNumber.setConfirmedBy(null);
//        userByNumber.setCreatedBy("anonymousUser");
        userByNumber.setActive(false);
        userByNumber.clearRoles();

        userByNumber.setConfirmedTs(null);
        userByNumber.setConfirmationNumber(getRandomConfirmationNumber());
        userByNumber.setConfirmationNumberCreatedTs(LocalDateTime.now());
        userByNumber.setTelNumber(numberDto.getTelNumber());
        userByNumber.setUsername(numberDto.getTelNumber());
        userByNumber.setPassword(passwordEncoder.encode("test"));
        userByNumber.setFirstName("");
        userByNumber.setLastName("");
        log.info("Registering new user {}", userByNumber.getUsername());
        userService.addOne(userByNumber);
        log.info("Sending confirmation number {}", numberDto.getTelNumber());
        smsSender.sendSms(userByNumber.getTelNumber(), smsProperties.getTrialNumber(), "Welcome to SanKaz! Your confirmation number is: " + userByNumber.getConfirmationNumber());
        log.info("End of sending confirmation number {}", numberDto.getTelNumber());
        return userByNumber.getConfirmationNumber();
    }

    @Override
    public int sendResetNumber(NumberDto numberDto) {
        log.info("Start of sending confirmation number {}", numberDto.getTelNumber());
        log.info("Checking tel number in DB");
        SecUser userByNumber = userService.getUserByTelNumber(numberDto.getTelNumber());
        userByNumber.setResetNumber(getRandomConfirmationNumber());
        userByNumber.setResetNumberCreatedTs(LocalDateTime.now());
        log.info("Updating user {}", userByNumber.getUsername());
        userService.editOneById(userByNumber);
        log.info("Sending confirmation number {}", numberDto.getTelNumber());
        smsSender.sendSms(userByNumber.getTelNumber(), smsProperties.getTrialNumber(), "Hello from SanKaz! Your reset number is: " + userByNumber.getResetNumber());
        log.info("End of sending confirmation number {}", numberDto.getTelNumber());
        return userByNumber.getResetNumber();
    }

    @Override
    public void checkConfirmationNumber(RegisterDto registerDto) {
        log.info("Start of checking confirmation number {}", registerDto.getTelNumber());
        log.info("Checking tel number in DB");
        SecUser userByNumber = userService.getUserByTelNumber(registerDto.getTelNumber());

        if (LocalDateTime.now().isAfter(userByNumber.getConfirmationNumberCreatedTs().plusMinutes(CONFIRMATION_TIME_IN_MINUTES))) {
            log.info("Confirmation number time is expired! {}", registerDto.getConfirmationNumber());
            throw new RuntimeException("Confirmation number time is expired! Please, reset again!");
        }
        if (userByNumber.getConfirmationNumber() != registerDto.getConfirmationNumber()) {
            log.info("Invalid confirmation number! {}", registerDto.getConfirmationNumber());
            throw new RuntimeException("Invalid confirmation number!");
        }
        userByNumber.setConfirmationNumber(0);
        userByNumber.setConfirmedTs(LocalDateTime.now());
        userByNumber.setConfirmedBy(userByNumber.getUsername());
        userByNumber.setCreatedBy(userByNumber.getUsername());
        userByNumber.setActive(true);
        log.info("Searching for role {}", "ROLE_USER");
        SecRole roleUser = roleService.getByName("ROLE_USER");
        userByNumber.addRole(roleUser);
        log.info("Updating user after confirmation number {}", registerDto.getConfirmationNumber());
        userService.editOneById(userByNumber);
        log.info("End of checking confirmation number {}", registerDto.getTelNumber());
    }

    @Override
    public void checkResetNumber(RegisterDto registerDto) {
        log.info("Start of checking reset number {}", registerDto.getTelNumber());
        log.info("Checking tel number in DB");
        SecUser userByNumber = userService.getUserByTelNumber(registerDto.getTelNumber());

        if (userByNumber.getResetNumber() == 0) {
            log.info("Unable to check reset number! {}", registerDto.getResetNumber());
            throw new RuntimeException("Unable to check reset number. Please, send new one.");
        }
        if (LocalDateTime.now().isAfter(userByNumber.getResetNumberCreatedTs().plusMinutes(CONFIRMATION_TIME_IN_MINUTES))) {
            log.info("Reset number time is expired! {}", registerDto.getResetNumber());
            throw new RuntimeException("Reset number time is expired! Please, reset again!");
        }
        if (userByNumber.getResetNumber() != registerDto.getResetNumber()) {
            log.info("Invalid reset number! {}", registerDto.getResetNumber());
            throw new RuntimeException("Invalid reset number!");
        }
        userByNumber.setResetNumber(0);
        log.info("Updating user after reset number {}", registerDto.getResetNumber());
        userService.editOneById(userByNumber);
        log.info("End of checking reset number {}", registerDto.getTelNumber());
    }

    @Override
    public SecUserDto finishRegistration(FinishRegDto finishRegDto) {
        log.info("Start of finishing registration {}", finishRegDto.getTelNumber());
        log.info("Checking tel number in DB");
        SecUser userByNumber = userService.getUserByTelNumber(finishRegDto.getTelNumber());

        userByNumber.setPassword(passwordEncoder.encode(finishRegDto.getPassword()));
        userByNumber.setFirstName(finishRegDto.getFirstName());
        userByNumber.setLastName(finishRegDto.getLastName());
        userByNumber.setCity(finishRegDto.getCity());
        log.info("Updating user {}", finishRegDto.getTelNumber());
        userService.editOneById(userByNumber);
        log.info("End of finishing registration {}", finishRegDto.getTelNumber());

        return userMapper.userToDto(userByNumber);
    }

    @Override
    public SecUserDto resetPassword(ResetPasswordDto resetPasswordDto) {
        log.info("Start of resetting password {}", resetPasswordDto.getTelNumber());
        log.info("Checking user by tel number in DB");
        SecUser userByNumber = userService.getUserByTelNumber(resetPasswordDto.getTelNumber());

        if(!resetPasswordDto.getPassword().equals(resetPasswordDto.getConfirmPassword())){
            throw new RuntimeException("Password mismatch!");
        }
        userByNumber.setResetNumberCreatedTs(null);
        userByNumber.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
        log.info("Updating user {}", resetPasswordDto.getTelNumber());
        userService.editOneById(userByNumber);
        log.info("End of resetting password {}", resetPasswordDto.getTelNumber());

        // TODO: MAKE OLD TOKEN INACTIVE

        return userMapper.userToDto(userByNumber);
    }

    private int getRandomConfirmationNumber() {
        return (int)(Math.random()*9000)+1000;
    }
}
