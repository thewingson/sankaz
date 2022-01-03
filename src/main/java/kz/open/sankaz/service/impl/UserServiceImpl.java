package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.SecRole;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.repo.RoleRepo;
import kz.open.sankaz.repo.UserRepo;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.MailSender;
import kz.open.sankaz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final MailSender mailSender;

    @Autowired
    @Lazy
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, MailSender mailSender) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.mailSender = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    /**
     * Простое админское создание пользователя
     * */
    @Override
    public void createUser(UserDetails user) {
        SecUser castedUser = (SecUser) user;
        SecUser currentUser = authService.getCurrentUser();

        castedUser.setPassword(passwordEncoder.encode(((SecUser) user).getPassword()));
        castedUser.setActive(true);
        castedUser.setConfirmedTs(LocalDateTime.now());
        castedUser.setConfirmedBy(currentUser != null ? currentUser.getUsername() : castedUser.getUsername());
        castedUser.setConfirmationId(UUID.randomUUID());
        List<SecRole> secRoles = roleRepo.findAllByNameIn(castedUser.getRoles().stream().map(SecRole::getName).collect(Collectors.toList()));
        castedUser.setRoles(secRoles);

        userRepo.save(castedUser);
    }

    @Override
    public void updateUser(UserDetails user) {
        userRepo.save((SecUser) user);
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
    public SecUser getUserByConfirmationId(UUID confirmationId) {
        return userRepo.findByConfirmationId(confirmationId);
    }

    @Override
    public List<SecUser> getUsers(Map<String, Object> params) {
        return userRepo.findAll();
    }

    @Override
    public SecUser addUserForRunner(SecUser user) {
        SecUser castedUser = (SecUser) user;
        castedUser.setPassword(passwordEncoder.encode(((SecUser) user).getPassword()));
        castedUser.setActive(true);
        castedUser.setConfirmedTs(LocalDateTime.now());
        castedUser.setConfirmedBy("admin");
        castedUser.setConfirmationId(UUID.randomUUID());

        userRepo.save(castedUser);
        return user;
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
