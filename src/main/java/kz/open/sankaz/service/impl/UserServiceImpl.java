package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.exception.MessageCodeException;
import kz.open.sankaz.exception.UserCodes;
import kz.open.sankaz.mapper.SecUserMapper;
import kz.open.sankaz.model.*;
import kz.open.sankaz.model.enums.ConfirmationStatus;
import kz.open.sankaz.model.enums.OrganizationConfirmationStatus;
import kz.open.sankaz.model.enums.UserType;
import kz.open.sankaz.pojo.dto.PageDto;
import kz.open.sankaz.pojo.dto.PictureDto;
import kz.open.sankaz.pojo.dto.SecUserDto;
import kz.open.sankaz.pojo.filter.SecUserEditFilter;
import kz.open.sankaz.pojo.filter.UserCreateFilter;
import kz.open.sankaz.pojo.filter.UserEditFilter;
import kz.open.sankaz.repo.UserRepo;
import kz.open.sankaz.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@Slf4j
public class UserServiceImpl extends AbstractService<SecUser, UserRepo> implements UserService {

    @Autowired
    private SysFileService fileService;

    @Autowired
    private GenderService genderService;

    @Autowired
    private CityService cityService;

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private SecUserMapper userMapper;

    @Value("${application.file.upload.path.image}")
    private String APPLICATION_UPLOAD_PATH_IMAGE;

    @Value("${application.file.download.path.image}")
    private String APPLICATION_DOWNLOAD_PATH_IMAGE;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    private OrganizationService organizationService;

    @Lazy
    @Autowired
    private BookingService bookingService;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        super(userRepo);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.getByUsername(username);
    }

    @Override
    public void createUser(UserDetails user) {
        addOne((SecUser) user);
    }

    @Override
    public void updateUser(UserDetails user) {
        editOneById((SecUser) user);
    }


    @Override
    public void deleteUser(String username) {
        deleteOneByUsernameSoft(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new RuntimeException("Method is not supported!");
    }

    @Override
    public boolean userExists(String username) {
        return loadUserByUsername(username) != null;
    }

    @Override
    public List<SecUser> getAll() {
        return repo.getAllByActive(true);
    }

    @Override
    public void deleteOneByUsernameSoft(String username) {
        SecUser user = (SecUser)loadUserByUsername(username);
        repo.save(user);
    }

    @Override
    public SecUser getUserByTelNumber(String telNumber) throws EntityNotFoundException {
        Optional<SecUser> secUser = repo.findByTelNumber(telNumber);
        if(!secUser.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("TEL_NUMBER", telNumber);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return secUser.get();
    }

    @Override
    public SecUser updateProfile(Long id, SecUserEditFilter filter) {
        SecUser user = getOne(id);
        user.setFirstName(filter.getFirstName());
        user.setLastName(filter.getLastName());
        user.setEmail(filter.getEmail());
        if (filter.getGenderId() != null) {
            Gender gender = genderService.getOne(filter.getGenderId());
            if(!gender.equals(user.getGender())){
                user.setGender(gender);
            }
        }
        if(filter.getCityId() != null){
            City city = cityService.getOne(filter.getCityId());
            if(!city.equals(user.getCity())){
                user.setCity(city);
            }
        }
        return user;
    }

    @Override
    public SecUser createOne(UserCreateFilter filter) {
        SecUser userByNumber = new SecUser();
        try {
            getUserByTelNumber(filter.getTelNumber());
            throw new MessageCodeException(UserCodes.TEL_NUMBER_IS_ALREADY_REGISTERED);
        } catch (EntityNotFoundException e) {
            log.info("Tel. number did not registered {}", filter.getTelNumber());
        }

        if(filter.getCityId() != null){
            City city = cityService.getOne(filter.getCityId());
            userByNumber.setCity(city);
        }
        if(filter.getGenderId() != null){
            Gender gender = genderService.getOne(filter.getGenderId());
            userByNumber.setGender(gender);
        }
        if(!filter.getEmail().isEmpty()){
            try{
                getUserByEmail(filter.getEmail());
                throw new MessageCodeException(UserCodes.EMAIL_IS_ALREADY_REGISTERED);
            } catch (EntityNotFoundException e){
                userByNumber.setEmail(filter.getEmail());
            }
        }
        userByNumber.setPassword(passwordEncoder.encode("Welcome123"));
        userByNumber.setConfirmationStatus(ConfirmationStatus.valueOf(filter.getConfirmationStatus()));
        userByNumber.setUserType(UserType.valueOf(filter.getUserType()));
        userByNumber.setUsername(filter.getUsername());
        userByNumber.setEmail(filter.getEmail());
        userByNumber.setTelNumber(filter.getTelNumber());
        userByNumber.setFirstName(filter.getFirstName());
        userByNumber.setLastName(filter.getLastName());
        userByNumber.setActive(true);
        userByNumber.setConfirmedBy("admin");
        userByNumber.setConfirmedDate(LocalDateTime.now());

        return addOne(userByNumber);
    }

    @Override
    public SecUser editOne(Long userId, UserEditFilter filter) {
        SecUser userByNumber = getOne(userId);
        try {
            SecUser userByTelNumber = getUserByTelNumber(filter.getTelNumber());
            if(!userByNumber.getId().equals(userByTelNumber.getId())){
                throw new MessageCodeException(UserCodes.TEL_NUMBER_IS_ALREADY_REGISTERED);
            }
        } catch (EntityNotFoundException e) {
            userByNumber.setUsername(filter.getTelNumber());
            userByNumber.setTelNumber(filter.getTelNumber());
        }
        if(!filter.getEmail().isEmpty()){
            try{
                SecUser userByEmail = getUserByEmail(filter.getEmail());
                if(!userByNumber.getId().equals(userByEmail.getId())){
                    throw new MessageCodeException(UserCodes.EMAIL_IS_ALREADY_REGISTERED);
                }
            } catch (EntityNotFoundException e){
                userByNumber.setEmail(filter.getEmail());
            }
        }
        if(filter.getCityId() != null){
            City city = cityService.getOne(filter.getCityId());
            userByNumber.setCity(city);
        }
        if(filter.getGenderId() != null){
            Gender gender = genderService.getOne(filter.getGenderId());
            userByNumber.setGender(gender);
        }
        userByNumber.setConfirmationStatus(ConfirmationStatus.valueOf(filter.getConfirmationStatus()));
        userByNumber.setUserType(UserType.valueOf(filter.getUserType()));
        userByNumber.setFirstName(filter.getFirstName());
        userByNumber.setLastName(filter.getLastName());

        return editOneById(userByNumber);
    }

    @Override
    public SysFile addPic(Long userId, MultipartFile pic) throws IOException {
        log.info(getServiceClass().getSimpleName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " Started");
        SecUser user = getOne(userId);

        if (!pic.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(APPLICATION_UPLOAD_PATH_IMAGE);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + pic.getOriginalFilename();
            String fileNameWithPath = APPLICATION_UPLOAD_PATH_IMAGE + "/" + resultFilename;

            pic.transferTo(new File(fileNameWithPath));

            SysFile file = new SysFile();
            file.setFileName(resultFilename);
            file.setExtension(pic.getContentType());
            file.setSize(pic.getSize());
            file = sysFileService.addOne(file);

            user.setPic(file);
        }

        return editOneById(user).getPic();
    }

    @Override
    public void deletePic(Long userId) {
        log.info(getServiceClass().getSimpleName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " Started");
        SecUser user = getOne(userId);
        SysFile pic = user.getPic();
        pic.setDeletedDate(LocalDate.now());
        sysFileService.editOneById(pic);
        user.setPic(null);
        editOneById(user);
    }

    @Override
    public SecUserDto changePassword(Long id, String password, String confirmPassword) {
        SecUser user = getOne(id);
//        if(password.equals(confirmPassword)){
//            throw new RuntimeException("It matches with old password!");
//        }
        if(!password.equals(confirmPassword)){
            throw new RuntimeException("Пароли не совпадают");
        }
        user.setPassword(passwordEncoder.encode(password));
        return userMapper.userToDto(editOneById(user));
    }

    @Override
    public PictureDto changePicture(Long userId, MultipartFile file) throws IOException {
        SecUser user = getOne(userId);
        String fileNameWithPath = "";
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new RuntimeException("Wrong file!");
        }

        File uploadDir = new File(APPLICATION_UPLOAD_PATH_IMAGE);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();
        fileNameWithPath = APPLICATION_UPLOAD_PATH_IMAGE + "/" + resultFilename;

        file.transferTo(new File(fileNameWithPath));

        SysFile sysFile = new SysFile();
        sysFile.setExtension(file.getContentType());
        sysFile.setFileName(file.getOriginalFilename());
        sysFile.setSize(file.getSize());
        fileService.addOne(sysFile);

        user.setPic(sysFile);
        editOneById(user);
        return new PictureDto(APPLICATION_DOWNLOAD_PATH_IMAGE + "/" + resultFilename);
    }

    @Override
    public void deletePicture(Long userId) {
        // TODO: remove pic file
        SecUser user = getOne(userId);
        user.setPic(null);
        editOneById(user);
    }

    @Override
    public SecUser getUserByEmail(String email) {
        Optional<SecUser> secUser = repo.findByEmail(email);
        if(!secUser.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("email", email);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return secUser.get();
    }

    @Override
    public SecUser getUserByUsername(String username) {
        Optional<SecUser> secUser = repo.findByUsername(username);
        if(!secUser.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("username", username);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return secUser.get();
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = SQLException.class)
    @Override
    public void deleteOneById(Long id) {
        SecUser user = getOne(id);
        List<Organization> organizationList = new ArrayList<>();
        user.getOrganizations().forEach(organization -> {
            organizationList.add(organization);
            organization.setUser(null);
            organization.setConfirmationStatus(OrganizationConfirmationStatus.REJECTED);
            organization.setRejectMessage("User is deleted");
            organization.setEmail("deleted_" + organization.getEmail());
            organization.setIban("deleted_" + organization.getIban());
            organization.setIin("deleted_" + organization.getIin());
            organization.setTelNumber("deleted_" + organization.getTelNumber());
        });
        organizationService.saveAll(organizationList);

        List<Booking> bookings = new ArrayList<>();
        bookingService.getAllByUser(user).forEach(booking -> {
            bookings.add(booking);
            booking.setUser(null);
        });
        bookingService.saveAll(bookings);

        user.setUsername("deleted_" + user.getUsername());
        user.setTelNumber("deleted_" + user.getTelNumber());
        user.setEmail("deleted_" + user.getEmail());
        user.setActive(false);
        repo.save(user);
    }

    @Override
    public PageDto getAllPage(int page, int size) {
        Page<SecUser> pages = getAll(page, size);
        PageDto dto = new PageDto();
        dto.setTotal(pages.getTotalElements());
        dto.setContent(userMapper.userToDto(pages.getContent()));
        dto.setPageable(pages.getPageable());
        return dto;
    }

    @Override
    protected Class getCurrentClass() {
        return SecUser.class;
    }

}
