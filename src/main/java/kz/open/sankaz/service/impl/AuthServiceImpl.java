package kz.open.sankaz.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kz.open.sankaz.config.SmscApi;
import kz.open.sankaz.exception.*;
import kz.open.sankaz.mapper.SecUserMapper;
import kz.open.sankaz.model.*;
import kz.open.sankaz.model.enums.ConfirmationStatus;
import kz.open.sankaz.model.enums.OrganizationConfirmationStatus;
import kz.open.sankaz.model.enums.ResetNumberStatus;
import kz.open.sankaz.model.enums.UserType;
import kz.open.sankaz.pojo.dto.ConfirmationStatusDto;
import kz.open.sankaz.pojo.dto.NumberFreeDto;
import kz.open.sankaz.pojo.dto.TokenDto;
import kz.open.sankaz.pojo.filter.FinishRegFilter;
import kz.open.sankaz.pojo.filter.OrganizationRegisterFinishFilter;
import kz.open.sankaz.pojo.filter.RegisterFilter;
import kz.open.sankaz.properties.MailProperties;
import kz.open.sankaz.properties.SecurityProperties;
import kz.open.sankaz.properties.SmsProperties;
import kz.open.sankaz.repo.SecUserTokenRepo;
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
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Transactional
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
    private CityService cityService;
    @Autowired
    private GenderService genderService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private SmsProperties smsProperties;
    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SecUserTokenRepo tokenRepo;

    @Autowired
    private SanService sanService;

    @Autowired
    private SecUserMapper userMapper;

    @Value("${security.account.confirm.link}")
    private String ACCOUNT_CONFIRM_LINK;

    private static final int CONFIRMATION_TIME_IN_MINUTES = 10;

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
                result.put("accessToken", accessToken);
                result.put("refreshToken", refreshToken);
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
    public int sendConfirmationNumber(String telNumber) {
        log.info("Start of sending confirmation number {}", telNumber);
        SecUser userByNumber;
        try{
            log.info("Checking tel number in DB");
            userByNumber = userService.getUserByTelNumber(telNumber);
            if(userByNumber.getConfirmedBy() != null){
                throw new RuntimeException("Номер уже зарегистрирован! Пожалуйста, укажите другой номер.");
            }
        } catch (EntityNotFoundException e){
            // FREE NUMBER
            log.info("Number is free");
            userByNumber = new SecUser();
        }

        userByNumber.setUserType(UserType.USER);
        userByNumber.setConfirmedBy(null);
        userByNumber.setActive(false);
        userByNumber.clearRoles();

        userByNumber.setConfirmationStatus(ConfirmationStatus.ON_CONFIRMATION);
        userByNumber.setConfirmedDate(null);
        userByNumber.setConfirmationNumber(getRandomConfirmationNumber());
        userByNumber.setConfirmationNumberCreatedDate(LocalDateTime.now());
        userByNumber.setTelNumber(telNumber);
        userByNumber.setUsername(telNumber);
        userByNumber.setPassword(passwordEncoder.encode("test"));
        userByNumber.setFirstName("");
        userByNumber.setLastName("");
        log.info("Registering new user {}", userByNumber.getUsername());
        userService.addOne(userByNumber);
        log.info("Sending confirmation number {}", telNumber);

        String phones = telNumber;
        String message = "Добро пожаловать в SanKaz! \nВаш номер подтверждения: " + userByNumber.getConfirmationNumber();
        int translit = 0;
        String time = "";
        String id = "";
        int format = 0;
        String sender = "SanKaz";
        String query = "";
        SmscApi smscApi = new SmscApi();
        smscApi.send_sms(phones, message, translit, time, id, format, sender, query);
//        smsSender.sendSms(userByNumber.getTelNumber(), smsProperties.getTrialNumber(), "Добро пожаловать в SanKaz! \nВаш номер подтверждения: " + userByNumber.getConfirmationNumber());
        log.info("End of sending confirmation number {}", telNumber);
        return userByNumber.getConfirmationNumber();
    }

    @Override
    public int sendConfirmationNumberOrganization(String telNumber, String password, String confirmPassword) {
        log.info("Start of sending confirmation number {}", telNumber);
        if(!password.equals(confirmPassword)){
            throw new RuntimeException("Пароли не совпадают! Пожалуйста, введите еще раз.");
        }
        SecUser userByNumber;
        try{
            log.info("Checking tel number in DB");
            userByNumber = userService.getUserByTelNumber(telNumber);
            if(userByNumber.getConfirmationStatus().equals(ConfirmationStatus.CONFIRMED)){
                throw new RuntimeException("К сожалению номер занят! Пожалуйста, введите другой номер.");
            }
        } catch (EntityNotFoundException e){
            // FREE NUMBER
            log.info("Number is free");
            userByNumber = new SecUser();
        }

        userByNumber.setUserType(UserType.ORG);
        userByNumber.setConfirmedBy(null);
        userByNumber.setActive(false);
        userByNumber.clearRoles();

        userByNumber.setConfirmationStatus(ConfirmationStatus.ON_CONFIRMATION);
        userByNumber.setConfirmedDate(null);
        userByNumber.setConfirmationNumber(getRandomConfirmationNumber());
        userByNumber.setConfirmationNumberCreatedDate(LocalDateTime.now());
        userByNumber.setTelNumber(telNumber);
        userByNumber.setUsername(telNumber);
        userByNumber.setPassword(password);
        userByNumber.setFirstName("");
        userByNumber.setLastName("");
        log.info("Registering new user {}", userByNumber.getUsername());
        userService.addOne(userByNumber);
        log.info("Sending confirmation number {}", telNumber);

        String phones = telNumber;
        String message = "Добро пожаловать в SanKaz! \nВаш номер подтверждения: " + userByNumber.getConfirmationNumber();
        int translit = 0;
        String time = "";
        String id = "";
        int format = 0;
        String sender = "SanKaz";
        String query = "";
        SmscApi smscApi = new SmscApi();
        smscApi.send_sms(phones, message, translit, time, id, format, sender, query);
//        smsSender.sendSms(userByNumber.getTelNumber(), smsProperties.getTrialNumber(), "Добро пожаловать в SanKaz! \nВаш номер подтверждения: " + userByNumber.getConfirmationNumber());
        log.info("End of sending confirmation number {}", telNumber);
        return userByNumber.getConfirmationNumber();
    }

    @Override
    public int sendResetNumber(String telNumber) {
        log.info("Start of sending confirmation number {}", telNumber);
        log.info("Checking tel number in DB");
        SecUser userByNumber = null;
        try{
            userByNumber = userService.getUserByTelNumber(telNumber);
        } catch (EntityNotFoundException e){
            throw new RuntimeException("Неправильный номер! Данный номер отсутствует базе.");
        }

        userByNumber.setResetNumberStatus(ResetNumberStatus.ON_RESET);
        userByNumber.setResetNumber(getRandomConfirmationNumber());
        userByNumber.setResetNumberCreatedDate(LocalDateTime.now());
        userService.editOneById(userByNumber);

        String phones = userByNumber.getTelNumber();
        String message = "SanKaz приветствует Вас! \nВаш номер для сброса пароля: " + userByNumber.getResetNumber();
        int translit = 0;
        String time = "";
        String id = "";
        int format = 0;
        String sender = "SanKaz";
        String query = "";
        SmscApi smscApi = new SmscApi();
        smscApi.send_sms(phones, message, translit, time, id, format, sender, query);
//        smsSender.sendSms(userByNumber.getTelNumber(), smsProperties.getTrialNumber(), message);
        return userByNumber.getResetNumber();
    }

    @Override
    public void checkConfirmationNumber(RegisterFilter filter) {
        log.info("Start of checking confirmation number {}", filter.getTelNumber());
        log.info("Checking tel number in DB");
        SecUser userByNumber = userService.getUserByTelNumber(filter.getTelNumber());

        if (userByNumber.getConfirmationStatus().equals(ConfirmationStatus.CONFIRMED)) {
            log.info("Number has already confirmed! {}", filter.getConfirmationNumber());
            throw new NumberConfirmationException(NumberConfirmationExceptionMessages.NUMBER_ALREADY_CONFIRMED_CODE);
        }
        if (LocalDateTime.now().isAfter(userByNumber.getConfirmationNumberCreatedDate().plusMinutes(CONFIRMATION_TIME_IN_MINUTES))) {
            log.info("Confirmation number time is expired! {}", filter.getConfirmationNumber());
            throw new NumberConfirmationException(NumberConfirmationExceptionMessages.CONFIRMATION_TIME_IS_EXPIRED);
        }
        if (userByNumber.getConfirmationNumber() != filter.getConfirmationNumber()) {
            log.info("Invalid confirmation number! {}", filter.getConfirmationNumber());
            throw new NumberConfirmationException(NumberConfirmationExceptionMessages.INVALID_CONFIRMATION_NUMBER);
        }
        userByNumber.setConfirmationStatus(ConfirmationStatus.CONFIRMED);
        userByNumber.setConfirmationNumber(0);
        userByNumber.setConfirmedDate(LocalDateTime.now());
        userByNumber.setConfirmedBy(userByNumber.getUsername());
        userByNumber.setActive(true);
        log.info("Searching for role");
        SecRole roleUser = roleService.getByName("ROLE_USER");
        userByNumber.addRole(roleUser);
        log.info("Updating user after confirmation number {}", filter.getConfirmationNumber());
        userService.editOneById(userByNumber);
        log.info("End of checking confirmation number {}", filter.getTelNumber());
    }

    @Override
    public TokenDto checkConfirmationNumberOrganization(String telNumber, String password, int confirmNumber) {
        log.info("Start of checking confirmation number {}", telNumber);
        log.info("Checking tel number in DB");
        SecUser userByNumber = null;
        try{
            userByNumber = userService.getUserByTelNumber(telNumber);
        } catch (EntityNotFoundException e){
            throw new RuntimeException("Неправильный номер или пароль! н");
        }
        if(!password.equals(userByNumber.getPassword())){
            throw new RuntimeException("Неправильный номер или пароль! п");
        }

        if (userByNumber.getConfirmationStatus().equals(ConfirmationStatus.CONFIRMED)) {
            log.info("Number has already confirmed! {}", confirmNumber);
            throw new RuntimeException("Номер уже подтвержден! Пожалуйста, закончите регистрацию.");
        }
        if (LocalDateTime.now().isAfter(userByNumber.getConfirmationNumberCreatedDate().plusMinutes(CONFIRMATION_TIME_IN_MINUTES))) {
            log.info("Confirmation number time is expired! {}", confirmNumber);
            throw new RuntimeException("Время номера подтверждения истек! Пожалуйста, отправьте еще раз!");
        }
        if (userByNumber.getConfirmationNumber() != confirmNumber) {
            log.info("Invalid confirmation number! {}", confirmNumber);
            throw new RuntimeException("Неправильный номер подтверждения!");
        }
        userByNumber.setPassword(passwordEncoder.encode(password));
        userByNumber.setConfirmationStatus(ConfirmationStatus.CONFIRMED);
        userByNumber.setConfirmationNumber(0);
        userByNumber.setConfirmedDate(LocalDateTime.now());
        userByNumber.setConfirmedBy(userByNumber.getUsername());
        userByNumber.setActive(true);
        log.info("Searching for role");
        SecRole roleUser = roleService.getByName("ROLE_MODERATOR");
        userByNumber.addRole(roleUser);
        log.info("Updating user after confirmation number {}", confirmNumber);
        userService.editOneById(userByNumber);
        log.info("End of checking confirmation number {}", telNumber);

        return authenticateUser(userByNumber.getUsername(), password);
    }

    @Override
    public void checkResetNumber(String telNumber, int resetNumber) {
        log.info("Start of checking reset number {}", telNumber);
        log.info("Checking tel number in DB");
        SecUser userByNumber = null;
        try{
            userByNumber = userService.getUserByTelNumber(telNumber);
        } catch (EntityNotFoundException e){
            throw new RuntimeException("Неправильный номер! Данный номер отсутствует базе.");
        }

        if (userByNumber.getResetNumber() == 0) {
            log.info("Unable to check reset number! {}", resetNumber);
            throw new RuntimeException("Невозможно проверить номер. Пожалуйста, отправьте номер для сброса еще раз.");
        }
        if (LocalDateTime.now().isAfter(userByNumber.getResetNumberCreatedDate().plusMinutes(CONFIRMATION_TIME_IN_MINUTES))) {
            log.info("Reset number time is expired! {}", resetNumber);
            throw new RuntimeException("Время для сброса истек! Пожалуйста, отправьте номер для сброса еще раз.");
        }
        if (userByNumber.getResetNumber() != resetNumber) {
            log.info("Invalid reset number! {}", resetNumber);
            throw new RuntimeException("Неправильный номер сброса!");
        }
        userByNumber.setResetNumberStatus(ResetNumberStatus.EMPTY);
        userByNumber.setResetNumber(0);
        log.info("Updating user after reset number {}", resetNumber);
        userService.editOneById(userByNumber);
        log.info("End of checking reset number {}", telNumber);
    }

    @Override
    public NumberFreeDto isNumberFree(String telNumber) {
        log.info("Start of checking number {}", telNumber);
        NumberFreeDto dto = new NumberFreeDto();
        boolean isFree = false;
        try{
            log.info("Checking tel number in DB");
            SecUser userByNumber = userService.getUserByTelNumber(telNumber);
            if(userByNumber.getConfirmationStatus().equals(ConfirmationStatus.ON_CONFIRMATION)
                    || userByNumber.getConfirmationStatus().equals(ConfirmationStatus.CONFIRMED)){
                userByNumber.setConfirmedDate(null);
                userByNumber.setConfirmedBy(null);
                userByNumber.setConfirmationNumber(getRandomConfirmationNumber());
                userByNumber.setConfirmationNumberCreatedDate(LocalDateTime.now());
                userByNumber.setConfirmationStatus(ConfirmationStatus.ON_CONFIRMATION);
                userService.editOneById(userByNumber);

                String phones = telNumber;
                String message = "Добро пожаловать в SanKaz! \nВаш номер подтверждения: " + userByNumber.getConfirmationNumber();
                int translit = 0;
                String time = "";
                String id = "";
                int format = 0;
                String sender = "SanKaz";
                String query = "";
                SmscApi smscApi = new SmscApi();
                smscApi.send_sms(phones, message, translit, time, id, format, sender, query);
            }
            dto.setConfirmationStatus(userByNumber.getConfirmationStatus().name());
            dto.setRoles(userByNumber.getRoles().stream().map(SecRole::getName).collect(Collectors.toList()));
        } catch (EntityNotFoundException e){
            isFree = true;
            dto.setConfirmationStatus("NEW");
        }
        log.info("End of checking number {}", telNumber);
        dto.setFree(isFree);
        return dto;
    }

    @Override
    public NumberFreeDto isNumberFreeOrganization(String telNumber) {
        log.info("Start of checking number {}", telNumber);
        NumberFreeDto dto = new NumberFreeDto();
        boolean isFree = false;
        try{
            log.info("Checking tel number in DB");
            SecUser userByNumber = userService.getUserByTelNumber(telNumber);
            dto.setConfirmationStatus(userByNumber.getConfirmationStatus().name());
            dto.setRoles(userByNumber.getRoles().stream().map(SecRole::getName).collect(Collectors.toList()));
        } catch (EntityNotFoundException e){
            isFree = true;
            dto.setConfirmationStatus(ConfirmationStatus.NEW.name());
        }
        log.info("End of checking number {}", telNumber);
        dto.setFree(isFree);
        return dto;
    }

    @Override
    public ConfirmationStatusDto getOrganizationConfirmationStatus(String telNumber){
        log.info("Start of checking organization confirmation status by number {}", telNumber);
        try{
            log.info("Checking tel number {} in DB", telNumber);
            Organization organization = organizationService.getOrganizationByTelNumber(telNumber);
            log.info("End of checking number {}", telNumber);
            return new ConfirmationStatusDto(organization.getId(),
                    organization.getConfirmationStatus().name(),
                    organization.getRejectMessage(),
                    organization.getRequestDate(),
                    organization.getApprovedDate(),
                    organization.getRejectedDate());
        } catch (EntityNotFoundException e){
            throw new RuntimeException("Указан неправильный номер для поиска!");
        }
    }

    @Override
    public Organization getOwnProfile(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC256(securityProperties.getSecurityTokenSecret().getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String username = decodedJWT.getSubject();

            SecUser user = (SecUser) userService.loadUserByUsername(username);
            Organization organization = organizationService.getOrganizationByUser(user);
            List<San> sans = sanService.getAllByIdIn(organization.getSans().stream().map(San::getId).collect(Collectors.toList()));
            organization.setSans(sans);
            return organization;
        }
        return null;
    }

    @Override
    public TokenDto finishRegistration(FinishRegFilter filter) {
        log.info("Start of finishing registration {}", filter.getTelNumber());
        log.info("Checking tel number in DB");
        SecUser userByNumber = userService.getUserByTelNumber(filter.getTelNumber());
        if(!userByNumber.getConfirmationStatus().equals(ConfirmationStatus.CONFIRMED)){
            throw new RuntimeException("This number is not confirmed! Please, confirm number before signing up.");
        }
        if(filter.getCityId() != null){
            City city = cityService.getOne(filter.getCityId());
            userByNumber.setCity(city);
        }
        if(filter.getGenderId() != null){
            Gender gender = genderService.getOne(filter.getGenderId());
            userByNumber.setGender(gender);
        }
        userByNumber.setConfirmationStatus(ConfirmationStatus.FINISHED);
        userByNumber.setPassword(passwordEncoder.encode(filter.getPassword()));
        userByNumber.setFirstName(filter.getFirstName());
        userByNumber.setLastName(filter.getLastName());

        userService.editOneById(userByNumber);

        return authenticateUser(userByNumber.getUsername(), filter.getPassword());
    }

    @Override
    public Organization registerOrganization(OrganizationRegisterFinishFilter filter) {
        SecUser userByNumber = userService.getUserByTelNumber(getCurrentUsername());
        if(!userByNumber.getConfirmationStatus().equals(ConfirmationStatus.CONFIRMED) && !userByNumber.getConfirmationStatus().equals(ConfirmationStatus.FINISHED)){
            throw new MessageCodeException(OrganizationCodes.NUMBER_IS_NOT_CONFIRMED);
        }
        if(userByNumber.getTelNumber().equals(filter.getTelNumber())){
            try{ // проверка org
                Organization organization = organizationService.getOrganizationByTelNumber(filter.getTelNumber());
                if(organization.getConfirmationStatus().equals(OrganizationConfirmationStatus.REJECTED)){
                    return registerOldOrganization(userByNumber, organization, filter);
                }
            } catch (EntityNotFoundException e){
                return registerNewOrganization(userByNumber, filter);
            }
        }
        return null;
    }

    private Organization registerNewOrganization(SecUser userByNumber, OrganizationRegisterFinishFilter filter) {
//        try{ // проверка email
//            userService.getUserByTelNumber(filter.getTelNumber());
//            throw new MessageCodeException(OrganizationCodes.TEL_NUMBER_IS_ALREADY_TAKEN);
//        } catch (EntityNotFoundException e){
//            log.info("Number is free {}", filter.getTelNumber());
//        }

        try{ // проверка iban
            organizationService.getOrganizationByIban(filter.getIban());
            throw new MessageCodeException(OrganizationCodes.IBAN_IS_ALREADY_TAKEN);
        } catch (EntityNotFoundException e){
            log.info("IBAN is free {}", filter.getTelNumber());
        }

        try{ // проверка iin
            organizationService.getOrganizationByIin(filter.getIin());
            throw new MessageCodeException(OrganizationCodes.IIN_IS_ALREADY_TAKEN);
        } catch (EntityNotFoundException e){
            log.info("IIN is free {}", filter.getTelNumber());
        }

        try{ // проверка номера в организациях
            organizationService.getOrganizationByTelNumber(filter.getTelNumber());
            throw new MessageCodeException(OrganizationCodes.TEL_NUMBER_IS_ALREADY_TAKEN);
        } catch (EntityNotFoundException e){
            log.info("Number is free {}", filter.getTelNumber());
        }
        try{ // проверка организации по юзеру / один юзер - одна организация !
            organizationService.getOrganizationByUser(userByNumber);
            throw new MessageCodeException(OrganizationCodes.ONE_USER_CAN_NOT_REGISTER_MULTIPLE_ORGANIZATIONS);
        } catch (EntityNotFoundException e){
            log.info("User did not registered any organization {}", userByNumber.getTelNumber());
        }

        userByNumber.setConfirmationStatus(ConfirmationStatus.FINISHED);
        userByNumber.setFirstName(filter.getFullName());
        userByNumber.setLastName(filter.getFullName());
        userService.editOneById(userByNumber);

        Organization organization = new Organization();
        organization.setRequestDate(LocalDateTime.now());
        organization.setConfirmationStatus(OrganizationConfirmationStatus.ON_CONFIRMATION);
        organization.setUser(userByNumber);
        organization.setIban(filter.getIban());
        organization.setName(filter.getOrgName());
        organization.setManagerFullName(filter.getFullName());
        organization.setTelNumber(filter.getTelNumber());
        organization.setIin(filter.getIin());
        organizationService.addOne(organization);
        // TODO: send to Moderator through method or listener
        return organization;
    }

    private Organization registerOldOrganization(SecUser userByNumber, Organization organization, OrganizationRegisterFinishFilter filter) {
        if (!organization.getTelNumber().equals(filter.getTelNumber())) {
            try { // проверка email
                userService.getUserByTelNumber(filter.getTelNumber());
                throw new MessageCodeException(OrganizationCodes.EMAIL_IS_ALREADY_TAKEN);
            } catch (EntityNotFoundException e) {
                organization.setTelNumber(filter.getTelNumber());
                userByNumber.setTelNumber(filter.getTelNumber());
            }
        }


        if(!organization.getIban().equals(filter.getIban())){
            try{ // проверка iban
                organizationService.getOrganizationByIban(filter.getIban());
                throw new MessageCodeException(OrganizationCodes.IBAN_IS_ALREADY_TAKEN);
            } catch (EntityNotFoundException e){
                organization.setIban(filter.getIban());
            }
        }

        if(!organization.getIin().equals(filter.getIin())){
            try{ // проверка iin
                organizationService.getOrganizationByIin(filter.getIin());
                throw new MessageCodeException(OrganizationCodes.IIN_IS_ALREADY_TAKEN);
            } catch (EntityNotFoundException e){
                organization.setIin(filter.getIin());
            }
        }

        if(!organization.getTelNumber().equals(filter.getTelNumber())){
            try{ // проверка номера в организациях
                organizationService.getOrganizationByTelNumber(filter.getTelNumber());
                throw new MessageCodeException(OrganizationCodes.TEL_NUMBER_IS_ALREADY_TAKEN);
            } catch (EntityNotFoundException e){
                organization.setTelNumber(filter.getTelNumber());
            }
        }

        userByNumber.setFirstName(filter.getFullName());
        userByNumber.setLastName(filter.getFullName());
        userService.editOneById(userByNumber);

        organization.setRequestDate(LocalDateTime.now());
        organization.setName(filter.getOrgName());
        organization.setConfirmationStatus(OrganizationConfirmationStatus.ON_CONFIRMATION);
        organization.setManagerFullName(filter.getFullName());
        organizationService.addOne(organization);
        // TODO: send to Moderator through method or listener
        return organization;
    }

    @Override
    public TokenDto resetPassword(String telNumber, String password, String confirmPassword) {
        SecUser userByNumber = userService.getUserByTelNumber(telNumber);

        if(userByNumber.getResetNumberStatus().equals(ResetNumberStatus.ON_RESET)){
            throw new RuntimeException("Вы не подтвердили номер сброса!");
        }
        if(!password.equals(confirmPassword)){
            throw new RuntimeException("Пароли не совподают!");
        }

        String encodedNewPassword = passwordEncoder.encode(password);
        if(encodedNewPassword.equals(userByNumber.getPassword())){
            throw new RuntimeException("Пароль не должен совподать со старым!");
        }

        userByNumber.setResetNumberCreatedDate(null);
        userByNumber.setPassword(passwordEncoder.encode(password));
        userService.editOneById(userByNumber);

        List<SecUserToken> tokens = tokenRepo.findAllByUser(userByNumber);
        tokens.forEach(token -> {
            token.setIsBlocked(true);
            token.setBlockDate(LocalDateTime.now());
        });
        tokenRepo.saveAll(tokens);

        return authenticateUser(userByNumber.getUsername(), password);
    }

    public TokenDto authenticateUser(String username, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        SecUser user = (SecUser) authentication.getPrincipal();

        Algorithm algorithm = Algorithm.HMAC256(securityProperties.getSecurityTokenSecret().getBytes());
        LocalDateTime accessTokenExpireDate = LocalDateTime.now().plusDays(7);
        LocalDateTime refreshTokenExpireDate = LocalDateTime.now().plusDays(7);
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Date.from(accessTokenExpireDate.atZone(ZoneId.systemDefault()).toInstant()))
                .withIssuer("/users/auth/finish-reg")
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("userId", user.getId())
                .sign(algorithm);
        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Date.from(refreshTokenExpireDate.atZone(ZoneId.systemDefault()).toInstant()))
                .withIssuer("/users/auth/finish-reg")
                .sign(algorithm);

        SecUserToken userToken = new SecUserToken();
        userToken.setUser(user);
        userToken.setAccessToken(accessToken);
        userToken.setExpireDate(accessTokenExpireDate);
        tokenRepo.save(userToken);
        return new TokenDto(accessToken, refreshToken, user.getId());
    }



    @Override
    public boolean checkIfOwnProfile(Long userId) {
        String currentUsername = getCurrentUsername();
        SecUser user = userService.getUserByTelNumber(currentUsername);
        if(!userId.equals(user.getId())){
            throw new RuntimeException("Данный профиль не является вашим!");
        }
        return true;
    }

    @Override
    public String getTokenFromAuthorization(String authorization) {
        return authorization.substring("Bearer ".length());
    }

    @Override
    public Long getUserIdFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(securityProperties.getSecurityTokenSecret().getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
//        String username = decodedJWT.getSubject();

        return decodedJWT.getClaim("userId").asLong();
    }

    @Override
    public Long getUserId(HttpServletRequest request){
        String header = request.getHeader(AUTHORIZATION);
        if(header == null){
            return -1l;
        }
        String tokenFromAuthorization = getTokenFromAuthorization(header);
        return getUserIdFromToken(tokenFromAuthorization);
    }

    private int getRandomConfirmationNumber() {
        return (int)(Math.random()*9000)+1000;
    }
}
