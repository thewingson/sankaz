package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.listener.event.AfterDeleteEvent;
import kz.open.sankaz.listener.event.BeforeDeleteEvent;
import kz.open.sankaz.mapper.SecUserMapper;
import kz.open.sankaz.model.*;
import kz.open.sankaz.pojo.dto.PictureDto;
import kz.open.sankaz.pojo.dto.SecUserDto;
import kz.open.sankaz.pojo.filter.SecUserEditFilter;
import kz.open.sankaz.repo.UserRepo;
import kz.open.sankaz.service.CityService;
import kz.open.sankaz.service.GenderService;
import kz.open.sankaz.service.SysFileService;
import kz.open.sankaz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    private SecUserMapper userMapper;

    @Value("${application.file.upload.path.image}")
    private String APPLICATION_UPLOAD_PATH_IMAGE;

    @Value("${application.file.download.path.image}")
    private String APPLICATION_DOWNLOAD_PATH_IMAGE;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public void deleteOneByUsernameSoft(String username) {
        SecUser user = (SecUser)loadUserByUsername(username);

        BeforeDeleteEvent beforeDeleteEvent = getBeforeDeleteEvent(user);
        if(beforeDeleteEvent != null){
            applicationEventPublisher.publishEvent(beforeDeleteEvent);
        }

        repo.save(user);

        AfterDeleteEvent afterDeleteEvent = getAfterDeleteEvent(user);
        if(afterDeleteEvent != null){
            applicationEventPublisher.publishEvent(afterDeleteEvent);
        }
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

    @Override
    protected Class getCurrentClass() {
        return SecUser.class;
    }

}
