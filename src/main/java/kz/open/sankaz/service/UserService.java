package kz.open.sankaz.service;

import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.pojo.dto.PictureDto;
import kz.open.sankaz.pojo.dto.TokenDto;
import kz.open.sankaz.pojo.filter.SecUserEditFilter;
import kz.open.sankaz.pojo.filter.UserCreateFilter;
import kz.open.sankaz.pojo.filter.UserEditFilter;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService extends UserDetailsManager, CommonService<SecUser> {
    void deleteOneByUsernameSoft(String username);

    SecUser getUserByTelNumber(String telNumber);

    TokenDto changePassword(Long id, String password, String confirmPassword, String oldPassword);

    PictureDto changePicture(Long userId, MultipartFile file) throws IOException;
    void deletePicture(Long userId);

    SecUser getUserByEmail(String email);
    SecUser getUserByUsername(String email);

    SecUser updateProfile(Long id, SecUserEditFilter filter);

    SecUser createOne(UserCreateFilter filter);

    SecUser editOne(Long userId, UserEditFilter filter);

    SysFile addPic(Long userId, MultipartFile pic) throws IOException;

    void deletePic(Long userId);

    List<SecUser> getAllPageWithFilter(String fullName, String telNumber, int page, int size);
}
