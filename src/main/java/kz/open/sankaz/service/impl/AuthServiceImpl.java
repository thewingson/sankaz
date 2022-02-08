package kz.open.sankaz.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kz.open.sankaz.config.SmscApi;
import kz.open.sankaz.pojo.dto.*;
import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.mapper.SecUserMapper;
import kz.open.sankaz.model.Organization;
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
    private OrganizationService organizationService;
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
    private static final int CONFIRMATION_TIME_IN_MINUTES = 1;

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

        userByNumber.setUserType("USER");
        userByNumber.setConfirmedBy(null);
        userByNumber.setActive(false);
        userByNumber.clearRoles();

        userByNumber.setConfirmationStatus("ON_CONFIRMATION");
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

        String phones = numberDto.getTelNumber();
        String message = "Добро пожаловать в SanKaz! \nВаш номер подтверждения: " + userByNumber.getConfirmationNumber();
        int translit = 0;
        String time = "";
        String id = "";
        int format = 0;
        String sender = "SanKaz";
        String query = "";
        SmscApi smscApi = new SmscApi();
//        smscApi.send_sms(phones, message, translit, time, id, format, sender, query);
        smsSender.sendSms(userByNumber.getTelNumber(), smsProperties.getTrialNumber(), "Добро пожаловать в SanKaz! \nВаш номер подтверждения: " + userByNumber.getConfirmationNumber());
        log.info("End of sending confirmation number {}", numberDto.getTelNumber());
        return userByNumber.getConfirmationNumber();
    }

    @Override
    public int sendConfirmationNumberOrganization(ResetPasswordDto resetPasswordDto) {
        log.info("Start of sending confirmation number {}", resetPasswordDto.getTelNumber());
        if(resetPasswordDto.getTelNumber() == null || resetPasswordDto.getPassword().isEmpty()){
            throw new RuntimeException("Пожалуйста, введите номер!");
        }
        if(!resetPasswordDto.getPassword().equals(resetPasswordDto.getConfirmPassword())){
            throw new RuntimeException("Пароли не совпадают! Пожалуйста, введите еще раз.");
        }
        SecUser userByNumber;
        try{
            log.info("Checking tel number in DB");
            userByNumber = userService.getUserByTelNumber(resetPasswordDto.getTelNumber());
            if(userByNumber.getConfirmationStatus().equals("CONFIRMED")){
                throw new RuntimeException("К сожалению номер занят! Пожалуйста, введите другой номер.");
            }
        } catch (EntityNotFoundException e){
            // FREE NUMBER
            log.info("Number is free");
            userByNumber = new SecUser();
        }

        userByNumber.setUserType("ORG");
        userByNumber.setConfirmedBy(null);
        userByNumber.setActive(false);
        userByNumber.clearRoles();

        userByNumber.setConfirmationStatus("ON_CONFIRMATION");
        userByNumber.setConfirmedTs(null);
        userByNumber.setConfirmationNumber(getRandomConfirmationNumber());
        userByNumber.setConfirmationNumberCreatedTs(LocalDateTime.now());
        userByNumber.setTelNumber(resetPasswordDto.getTelNumber());
        userByNumber.setUsername(resetPasswordDto.getTelNumber());
        userByNumber.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
        userByNumber.setFirstName("");
        userByNumber.setLastName("");
        log.info("Registering new user {}", userByNumber.getUsername());
        userService.addOne(userByNumber);
        log.info("Sending confirmation number {}", resetPasswordDto.getTelNumber());

        String phones = resetPasswordDto.getTelNumber();
        String message = "Добро пожаловать в SanKaz! \nВаш номер подтверждения: " + userByNumber.getConfirmationNumber();
        int translit = 0;
        String time = "";
        String id = "";
        int format = 0;
        String sender = "SanKaz";
        String query = "";
        SmscApi smscApi = new SmscApi();
//        smscApi.send_sms(phones, message, translit, time, id, format, sender, query);
        smsSender.sendSms(userByNumber.getTelNumber(), smsProperties.getTrialNumber(), "Добро пожаловать в SanKaz! \nВаш номер подтверждения: " + userByNumber.getConfirmationNumber());
        log.info("End of sending confirmation number {}", resetPasswordDto.getTelNumber());
        return userByNumber.getConfirmationNumber();
    }

    @Override
    public int sendResetNumber(NumberDto numberDto) {
        log.info("Start of sending confirmation number {}", numberDto.getTelNumber());
        log.info("Checking tel number in DB");
        SecUser userByNumber = userService.getUserByTelNumber(numberDto.getTelNumber());
        userByNumber.setResetNumberStatus("ON_RESET");
        userByNumber.setResetNumber(getRandomConfirmationNumber());
        userByNumber.setResetNumberCreatedTs(LocalDateTime.now());
        log.info("Updating user {}", userByNumber.getUsername());
        userService.editOneById(userByNumber);
        log.info("Sending confirmation number {}", numberDto.getTelNumber());

        String phones = userByNumber.getTelNumber();
        String message = "SanKaz приветствует Вас! \nВаш номер для сброса пароля: " + userByNumber.getResetNumber();
        int translit = 0;
        String time = "";
        String id = "";
        int format = 0;
        String sender = "SanKaz";
        String query = "";
        SmscApi smscApi = new SmscApi();
//        smscApi.send_sms(phones, message, translit, time, id, format, sender, query);
        smsSender.sendSms(userByNumber.getTelNumber(), smsProperties.getTrialNumber(), "Добро пожаловать в SanKaz! \nВаш номер подтверждения: " + userByNumber.getConfirmationNumber());
        log.info("End of sending confirmation number {}", numberDto.getTelNumber());
        return userByNumber.getResetNumber();
    }

    @Override
    public void checkConfirmationNumber(RegisterDto registerDto) {
        log.info("Start of checking confirmation number {}", registerDto.getTelNumber());
        log.info("Checking tel number in DB");
        SecUser userByNumber = userService.getUserByTelNumber(registerDto.getTelNumber());

        if (userByNumber.getConfirmedBy() != null) {
            log.info("Number has already confirmed! {}", registerDto.getConfirmationNumber());
            throw new RuntimeException("Номер уже подтвержден! Пожалуйста, закончите регистрацию.");
        }
        if (LocalDateTime.now().isAfter(userByNumber.getConfirmationNumberCreatedTs().plusMinutes(CONFIRMATION_TIME_IN_MINUTES))) {
            log.info("Confirmation number time is expired! {}", registerDto.getConfirmationNumber());
            throw new RuntimeException("Время номера подтверждения истек! Пожалуйста, отправьте еще раз!");
        }
        if (userByNumber.getConfirmationNumber() != registerDto.getConfirmationNumber()) {
            log.info("Invalid confirmation number! {}", registerDto.getConfirmationNumber());
            throw new RuntimeException("Неправильный номер подтверждения!");
        }
        userByNumber.setConfirmationStatus("CONFIRMED");
        userByNumber.setConfirmationNumber(0);
        userByNumber.setConfirmedTs(LocalDateTime.now());
        userByNumber.setConfirmedBy(userByNumber.getUsername());
        userByNumber.setCreatedBy(userByNumber.getUsername());
        userByNumber.setActive(true);
        log.info("Searching for role");
        SecRole roleUser = roleService.getByName("ROLE_USER");
        userByNumber.addRole(roleUser);
        log.info("Updating user after confirmation number {}", registerDto.getConfirmationNumber());
        userService.editOneById(userByNumber);
        log.info("End of checking confirmation number {}", registerDto.getTelNumber());
    }

    @Override
    public TokenDto checkConfirmationNumberOrganization(RegisterOrganizationDto registerDto) {
        log.info("Start of checking confirmation number {}", registerDto.getTelNumber());
        log.info("Checking tel number in DB");
        SecUser userByNumber = userService.getUserByTelNumber(registerDto.getTelNumber());

        if (userByNumber.getConfirmedBy() != null) {
            log.info("Number has already confirmed! {}", registerDto.getConfirmationNumber());
            throw new RuntimeException("Номер уже подтвержден! Пожалуйста, закончите регистрацию.");
        }
        if (LocalDateTime.now().isAfter(userByNumber.getConfirmationNumberCreatedTs().plusMinutes(CONFIRMATION_TIME_IN_MINUTES))) {
            log.info("Confirmation number time is expired! {}", registerDto.getConfirmationNumber());
            throw new RuntimeException("Время номера подтверждения истек! Пожалуйста, отправьте еще раз!");
        }
        if (userByNumber.getConfirmationNumber() != registerDto.getConfirmationNumber()) {
            log.info("Invalid confirmation number! {}", registerDto.getConfirmationNumber());
            throw new RuntimeException("Неправильный номер подтверждения!");
        }
        userByNumber.setConfirmationStatus("CONFIRMED");
        userByNumber.setConfirmationNumber(0);
        userByNumber.setConfirmedTs(LocalDateTime.now());
        userByNumber.setConfirmedBy(userByNumber.getUsername());
        userByNumber.setCreatedBy(userByNumber.getUsername());
        userByNumber.setActive(true);
        log.info("Searching for role");
        SecRole roleUser = roleService.getByName("ROLE_ORG");
        userByNumber.addRole(roleUser);
        log.info("Updating user after confirmation number {}", registerDto.getConfirmationNumber());
        userService.editOneById(userByNumber);
        log.info("End of checking confirmation number {}", registerDto.getTelNumber());

        return authenticateUser(userByNumber.getUsername(), registerDto.getPassword());
    }

    @Override
    public void checkResetNumber(RegisterDto registerDto) {
        log.info("Start of checking reset number {}", registerDto.getTelNumber());
        log.info("Checking tel number in DB");
        SecUser userByNumber = userService.getUserByTelNumber(registerDto.getTelNumber());

        if (userByNumber.getResetNumber() == 0) {
            log.info("Unable to check reset number! {}", registerDto.getResetNumber());
            throw new RuntimeException("Невозможно проверить номер. Пожалуйста, отправьте номер для сброса еще раз.");
        }
        if (LocalDateTime.now().isAfter(userByNumber.getResetNumberCreatedTs().plusMinutes(CONFIRMATION_TIME_IN_MINUTES))) {
            log.info("Reset number time is expired! {}", registerDto.getResetNumber());
            throw new RuntimeException("Время для сброса истек! Пожалуйста, отправьте номер для сброса еще раз.");
        }
        if (userByNumber.getResetNumber() != registerDto.getResetNumber()) {
            log.info("Invalid reset number! {}", registerDto.getResetNumber());
            throw new RuntimeException("Неправильный номер сброса!");
        }
        userByNumber.setResetNumberStatus("EMPTY");
        userByNumber.setResetNumber(0);
        log.info("Updating user after reset number {}", registerDto.getResetNumber());
        userService.editOneById(userByNumber);
        log.info("End of checking reset number {}", registerDto.getTelNumber());
    }

    @Override
    public NumberFreeDto isNumberFree(String telNumber) {
        log.info("Start of checking number {}", telNumber);
        NumberFreeDto dto = new NumberFreeDto();
        boolean isFree = false;
        try{
            log.info("Checking tel number in DB");
            SecUser userByNumber = userService.getUserByTelNumber(telNumber);
            dto.setConfirmationStatus(userByNumber.getConfirmationStatus());
        } catch (EntityNotFoundException e){
            isFree = true;
            dto.setConfirmationStatus("NEW");
        }
        log.info("End of checking number {}", telNumber);
        dto.setFree(isFree);
        return dto;
    }

    @Override
    public ConfirmationStatusDto getOrganizationConfirmationStatus(NumberDto numberDto){
        log.info("Start of checking organization confirmation status by number {}", numberDto);
        try{
            log.info("Checking tel number {} in DB", numberDto.getTelNumber());
            Organization organization = organizationService.getOrganizationByTelNumber(numberDto.getTelNumber());
            log.info("End of checking number {}", numberDto.getTelNumber());
            return new ConfirmationStatusDto(organization.getConfirmationStatus());
        } catch (EntityNotFoundException e){
            throw new RuntimeException("Указан неправильный номер для поиска!");
        }
    }

    @Override
    public TokenDto finishRegistration(FinishRegDto finishRegDto) {
        log.info("Start of finishing registration {}", finishRegDto.getTelNumber());
        log.info("Checking tel number in DB");
        SecUser userByNumber = userService.getUserByTelNumber(finishRegDto.getTelNumber());
        if(!userByNumber.getConfirmationStatus().equals("CONFIRMED")){
            throw new RuntimeException("This number is not confirmed! Please, confirm number before signing up.");
        }

        userByNumber.setConfirmationStatus("FINISHED");
        userByNumber.setPassword(passwordEncoder.encode(finishRegDto.getPassword()));
        userByNumber.setFirstName(finishRegDto.getFirstName());
        userByNumber.setLastName(finishRegDto.getLastName());
        if(finishRegDto.getCity() != null && !finishRegDto.getCity().isEmpty()){
            userByNumber.setCity(finishRegDto.getCity());
        }
        if(finishRegDto.getEmail() != null && !finishRegDto.getEmail().isEmpty()){
            userByNumber.setEmail(finishRegDto.getEmail());
        }
        if(finishRegDto.getGender() != null && !finishRegDto.getGender().isEmpty()){
            userByNumber.setGender(finishRegDto.getGender());
        }
        log.info("Updating user {}", finishRegDto.getTelNumber());
        userService.editOneById(userByNumber);
        log.info("End of finishing registration {}", finishRegDto.getTelNumber());

        return authenticateUser(userByNumber.getUsername(), finishRegDto.getPassword());
    }

    @Override
    public void registerOrganization(RegisterOrgDto registerOrgDto) {
        log.info("Start of finishing registration {}", registerOrgDto.getTelNumber());
        log.info("Checking tel number in DB");
        SecUser userByNumber = userService.getUserByTelNumber(getCurrentUsername());
        if(!userByNumber.getConfirmationStatus().equals("CONFIRMED") && !userByNumber.getConfirmationStatus().equals("FINISHED")){
            throw new RuntimeException("Номер не подтвержден! Пожалуйста, подтвердите номер.");
        }

        try{ // проверка email
            userService.getUserByEmail(registerOrgDto.getEmail());
            log.warn("Email is busy {}", registerOrgDto.getEmail());
            throw new RuntimeException("Этот email занят другой организацией! Пожалуйста, введите другой email для огранизации.");
        } catch (EntityNotFoundException e){
            log.info("Email is free {}", registerOrgDto.getTelNumber());
        }

        try{ // проверка iban
            organizationService.getOrganizationByIban(registerOrgDto.getIban());
            log.warn("IBAN is busy {}", registerOrgDto.getIban());
            throw new RuntimeException("Этот IBAN занят другой организацией! Пожалуйста, введите другой IBAN для огранизации.");
        } catch (EntityNotFoundException e){
            log.info("IBAN is free {}", registerOrgDto.getTelNumber());
        }

        try{ // проверка iin
            organizationService.getOrganizationByIin(registerOrgDto.getIin());
            log.warn("IIN is busy {}", registerOrgDto.getIban());
            throw new RuntimeException("Этот ИИН занят другой организацией! Пожалуйста, введите другой ИИН для огранизации.");
        } catch (EntityNotFoundException e){
            log.info("IIN is free {}", registerOrgDto.getTelNumber());
        }

        if(!userByNumber.getConfirmationStatus().equals("CONFIRMED") && !userByNumber.getConfirmationStatus().equals("FINISHED")){
            throw new RuntimeException("Номер не подтвержден! Пожалуйста, подтвердите номер.");
        }
        try{ // проверка номера в организациях
            organizationService.getOrganizationByTelNumber(registerOrgDto.getTelNumber());
            log.warn("Number is busy {}", registerOrgDto.getTelNumber());
            throw new RuntimeException("Этот номер занят другой организацией! Пожалуйста, введите другой номер для огранизации.");
        } catch (EntityNotFoundException e){
            log.info("Number is free {}", registerOrgDto.getTelNumber());
        }
        try{ // проверка организации по юзеру / один юзер - одна организация !
            organizationService.getOrganizationByUser(userByNumber);
            log.warn("User already has registered organization {}", userByNumber.getTelNumber());
            throw new RuntimeException("Вы уже зарегистрировали организацию! Пользователь может создать только одну огранизацию.");
        } catch (EntityNotFoundException e){
            log.info("User did not registered any organization {}", userByNumber.getTelNumber());
        }

        userByNumber.setConfirmationStatus("FINISHED");
        userByNumber.setFirstName(registerOrgDto.getFullName());
        userByNumber.setLastName(registerOrgDto.getFullName());
        if(registerOrgDto.getEmail() != null || !registerOrgDto.getEmail().isEmpty()){
            userByNumber.setEmail(registerOrgDto.getEmail());
        }
        log.info("Updating user {}", registerOrgDto.getTelNumber());
        userService.editOneById(userByNumber);

        log.info("Creating organization");
        Organization organization = new Organization();
        organization.setConfirmationStatus("ON_CONFIRMATION");
        organization.setUser(userByNumber);
        organization.setEmail(registerOrgDto.getEmail());
        organization.setIban(registerOrgDto.getIban());
        organization.setName(registerOrgDto.getOrgName());
        organization.setManagerFullName(registerOrgDto.getFullName());
        organization.setTelNumber(registerOrgDto.getTelNumber());
        organization.setIin(registerOrgDto.getIin());
        organization.setManagerFullName(registerOrgDto.getFullName());
        organizationService.addOne(organization);
        // TODO: send to Moderator through method or listener
        log.info("Organization creation is finished");
        log.info("End of finishing registration {}", registerOrgDto.getTelNumber());
    }

    @Override
    public TokenDto resetPassword(ResetPasswordDto resetPasswordDto) {
        log.info("Start of resetting password {}", resetPasswordDto.getTelNumber());
        log.info("Checking user by tel number in DB");
        SecUser userByNumber = userService.getUserByTelNumber(resetPasswordDto.getTelNumber());

        if(userByNumber.getResetNumberStatus().equals("ON_RESET")){
            throw new RuntimeException("Вы не подтвердили номер сброса!");
        }
        if(!resetPasswordDto.getPassword().equals(resetPasswordDto.getConfirmPassword())){
            throw new RuntimeException("Пароли не совпвдвют!");
        }
        userByNumber.setResetNumberCreatedTs(null);
        userByNumber.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
        log.info("Updating user {}", resetPasswordDto.getTelNumber());
        userService.editOneById(userByNumber);
        log.info("End of resetting password {}", resetPasswordDto.getTelNumber());

        // TODO: MAKE OLD TOKEN INACTIVE

        return authenticateUser(userByNumber.getUsername(), resetPasswordDto.getPassword());
    }

    private TokenDto authenticateUser(String username, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        SecUser user = (SecUser) authentication.getPrincipal();

        Algorithm algorithm = Algorithm.HMAC256(securityProperties.getSecurityTokenSecret().getBytes());
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .withIssuer("/users/auth/finish-reg")
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000))
                .withIssuer("/users/auth/finish-reg")
                .sign(algorithm);
        return new TokenDto(accessToken, refreshToken);
    }

    private int getRandomConfirmationNumber() {
        return (int)(Math.random()*9000)+1000;
    }
}
