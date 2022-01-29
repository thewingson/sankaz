package kz.open.sankaz.service;

import kz.open.sankaz.model.SecUser;
import org.springframework.security.provisioning.UserDetailsManager;

public interface UserService extends UserDetailsManager, CommonService<SecUser> {
    void deleteOneByUsernameSoft(String username);

    SecUser getUserByTelNumber(String telNumber);
}
