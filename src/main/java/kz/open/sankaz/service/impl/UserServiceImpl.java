package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.SecRole;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.repo.RoleRepo;
import kz.open.sankaz.repo.UserRepo;
import kz.open.sankaz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    @Override
    public void createUser(UserDetails user) {
        Set<SecRole> roles = new HashSet<>();
        SecRole admin = roleRepo.findByName("ADMIN");
        if (admin != null) {
            roles.add(roleRepo.findByName("ADMIN"));
        }
        ((SecUser) user).setPassword(passwordEncoder.encode(((SecUser) user).getPassword()));
        ((SecUser) user).setRoles(roles);
        userRepo.save((SecUser) user);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {
        SecUser user = (SecUser) loadUserByUsername(username);
        userRepo.delete(user);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
    }

    @Override
    public boolean userExists(String username) {
        return loadUserByUsername(username) != null;
    }

    @Override
    public SecUser getUser(String username) {
        return (SecUser) loadUserByUsername(username);
    }

    @Override
    public List<SecUser> getUsers(Map<String, Object> params) {
        return userRepo.findAll();
    }

    @Override
    public SecRole createRole(SecRole role) {
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        SecUser user = (SecUser) loadUserByUsername(username);
        SecRole role = roleRepo.findByName(roleName);
        user.addRole(role);
        userRepo.save(user);
    }
}
