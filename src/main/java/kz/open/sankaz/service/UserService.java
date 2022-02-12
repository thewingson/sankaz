package kz.open.sankaz.service;

import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.pojo.dto.PictureDto;
import kz.open.sankaz.pojo.dto.SecUserDto;
import kz.open.sankaz.pojo.filter.SecUserEditFilter;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService extends UserDetailsManager, CommonService<SecUser> {
    void deleteOneByUsernameSoft(String username);

    SecUser getUserByTelNumber(String telNumber);

    SecUserDto changePassword(Long id, String password, String confirmPassword);

    PictureDto changePicture(Long userId, MultipartFile file) throws IOException;
    void deletePicture(Long userId);

    SecUser getUserByEmail(String email);
    SecUser getUserByUsername(String email);

    SecUser updateProfile(Long id, SecUserEditFilter filter);
}
