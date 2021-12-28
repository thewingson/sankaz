package kz.open.sankaz.service;

import kz.open.sankaz.model.SecRole;
import kz.open.sankaz.model.SecUser;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsManager {
    SecUser getUser(String username);
    List<SecUser> getUsers(Map<String, Object> params);

    SecRole createRole(SecRole role);
    void addRoleToUser(String username, String roleName);
}
