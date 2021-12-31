package kz.open.sankaz.service;

import kz.open.sankaz.model.SecRole;
import kz.open.sankaz.model.SecUser;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService extends UserDetailsManager {
    SecUser getUser(String username);
    SecUser getUserByConfirmationId(UUID confirmationId);
    List<SecUser> getUsers(Map<String, Object> params);
    SecUser addUser(SecUser user);
    UUID registerUser(SecUser user);
    void registerUserAndReturnConfirmationBody(SecUser user);

    SecRole createRole(SecRole role);
    void addRoleToUser(String username, String roleName);
}
