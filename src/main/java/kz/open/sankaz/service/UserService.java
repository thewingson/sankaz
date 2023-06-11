package kz.open.sankaz.service;

import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.pojo.dto.TokenDto;
import kz.open.sankaz.pojo.dto.notifications.UserNotificationDto;
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

    TokenDto changePassword(Long id, String password, String confirmPassword);

    boolean changePicture(Long userId, MultipartFile file) throws IOException;


    SecUser getUserByEmail(String email);
    SecUser getUserByUsername(String email);

    SecUser updateProfile(Long id, SecUserEditFilter filter);

    SecUser createOne(UserCreateFilter filter);

    SecUser editOne(Long userId, UserEditFilter filter);

    List<SecUser> getAllPageWithFilter(String fullName, String telNumber, int page, int size);

    UserNotificationDto getNotifications(Long userId, int page, int size);

    void viewNotification(Long notId);
}
