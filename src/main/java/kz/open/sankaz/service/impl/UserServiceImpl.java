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
import java.util.*;

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

    @Override
    public void createUser(UserDetails user) {
        SecUser castedUser = (SecUser) user;

        Set<SecRole> roles = new HashSet<>();
        SecRole admin = roleRepo.findByName("ROLE_ADMIN");
        if (admin != null) {
            roles.add(admin);
        }
        castedUser.setPassword(passwordEncoder.encode(((SecUser) user).getPassword()));
        castedUser.setRoles(roles);
        SecUser currentUser = authService.getCurrentUser();

        boolean isAdmin = false;
        if (currentUser != null) {
            isAdmin = currentUser.getRoles().stream().map(SecRole::getName).anyMatch(roleName -> roleName.equals("ROLE_ADMIN"));
        }

        if(isAdmin){
            castedUser.setActive(true);
            castedUser.setConfirmedTs(LocalDateTime.now());
            castedUser.setConfirmedBy(currentUser.getUsername());
        }
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
    public SecUser addUser(SecUser user) {
        createUser(user);
        return user;
    }

    @Override
    public UUID registerUser(SecUser user) {
        return addUser(user).getConfirmationId();
    }

    @Override
    public void registerUserAndReturnConfirmationBody(SecUser user) {
        user = addUser(user);

        String message = String.format("Hello, %s!" +
                "\nWelcome to SanKaz!" +
                "\nTo confirm activation, please, visit next link http://localhost:8080/auth/confirm-account?tokenId=%s",
                user.getFullName(),
                user.getConfirmationId());
        mailSender.sendMail(user.getEmail(), "Activation code", message);
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
