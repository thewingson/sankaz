package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.*;
import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.listener.event.AfterDeleteEvent;
import kz.open.sankaz.listener.event.BeforeDeleteEvent;
import kz.open.sankaz.mapper.SecUserMapper;
import kz.open.sankaz.model.ItemPic;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.repo.UserRepo;
import kz.open.sankaz.service.ItemPicService;
import kz.open.sankaz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
@Slf4j
public class UserServiceImpl extends AbstractService<SecUser, UserRepo> implements UserService {

    @Autowired
    private ItemPicService picService;

    @Autowired
    private SecUserMapper userMapper;

    @Value("${application.file.upload.path}")
    private String APPLICATION_UPLOAD_PATH;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        super(userRepo);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByDeletedByIsNullAndUsername(username);
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
    public SecUserDto updateOneDto(Long id, SecUserEditDto dto) {
        SecUser user = getOne(id);
        if(dto.getFirstName() != null && !dto.getFirstName().equals(user.getFirstName())){
            user.setFirstName(dto.getFirstName());
        }
        if(dto.getLastName() != null && !dto.getLastName().equals(user.getLastName())){
            user.setLastName(dto.getLastName());
        }
        if(dto.getGender() != null && !dto.getGender().equals(user.getGender())){
            user.setGender(dto.getGender());
        }
        if(dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())){
            user.setEmail(dto.getEmail());
        }
        if(dto.getCity() != null && !dto.getCity().equals(user.getCity())){
            user.setCity(dto.getCity());
        }
        return userMapper.userToDto(editOneById(user));
    }

    @Override
    public SecUserDto changePassword(Long id, ChangePasswordDto dto) {
        SecUser user = getOne(id);
        if(dto.getPassword() == null || dto.getConfirmPassword() == null
                || dto.getPassword().isEmpty() || dto.getConfirmPassword().isEmpty()){
            throw new RuntimeException("You cannot set an empty password!");
        }
        if(dto.getPassword().equals(user.getPassword())){
            throw new RuntimeException("It matches with old password!");
        }
        if(!dto.getPassword().equals(dto.getConfirmPassword())){
            throw new RuntimeException("Password mismatch!");
        }
        return userMapper.userToDto(editOneById(user));
    }

    @Override
    public PictureDto changePicture(Long userId, MultipartFile file) throws IOException {
        SecUser user = getOne(userId);
        String fileNameWithPath = "";
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new RuntimeException("Wrong file!");
        }

        File uploadDir = new File(APPLICATION_UPLOAD_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();
        fileNameWithPath = APPLICATION_UPLOAD_PATH + "/" + resultFilename;

        file.transferTo(new File(fileNameWithPath));

        ItemPicDto picDto = new ItemPicDto();
        picDto.setExtension(file.getContentType());
        picDto.setFileName(file.getOriginalFilename());
        picDto.setSize(String.valueOf(file.getSize()));
        ItemPic itemPic = picService.addOneDto(picDto);

        user.setPic(itemPic);
        editOneById(user);
        return new PictureDto(fileNameWithPath);
    }

    @Override
    public void deletePicture(Long userId) {
        SecUser user = getOne(userId);
        user.setPic(null);
        editOneById(user);
    }

    @Override
    public SecUser getUserByEmail(String email) {
        Optional<SecUser> secUser = repo.findByEmailAndDeletedByIsNull(email);
        if(!secUser.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("email", email);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return secUser.get();
    }

    @Override
    protected Class getCurrentClass() {
        return SecUser.class;
    }

//    @PreAuthorize("getUsernameById(id) == principal")
    @Override
    public SecUserDto getOneDto(Long id) {
        // TODO: return full info if own account, hide password if other's account
        SecUser user = getOne(id);
        return userMapper.userToDto(user);
    }

    @Override
    public List<SecUserDto> getAllDto() {
        return userMapper.userToDto(getAll());
    }

    @Override
    public List<SecUserDto> getAllDto(Map<String, Object> params) {
        return userMapper.userToDto(getAll(params));
    }

    @Override
    public SecUser addOneDto(SecUserDto dto) {
        return null;
    }

    @Override
    public SecUser updateOneDto(Long id, SecUserDto dto) {
        return null;
    }

    @Override
    public SecUser updateOneDto(Map<String, Object> params, SecUserDto dto) {
        return null;
    }
}
