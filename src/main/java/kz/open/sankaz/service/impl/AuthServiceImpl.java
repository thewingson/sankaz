package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.model.auth.AuthRequest;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.SmsSender;
import kz.open.sankaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    @Lazy
    private UserService userService;
    @Autowired
    private SmsSender smsSender;

    @Override
    public void signOut(AuthRequest authRequest) {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    }

    @Override
    public SecUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null
                && authentication.getPrincipal() != null
                && !authentication.getPrincipal().equals("anonymousUser")){
            return (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }

    @Override
    public void confirmAccount(String tokenId) {
        SecUser user = userService.getUserByConfirmationId(UUID.fromString(tokenId));
        user.setConfirmedTs(LocalDateTime.now());
        user.setConfirmedBy(user.getUsername());
        user.setActive(true);
        userService.updateUser(user);
        smsSender.sendSms("+77081997727", "Kalaisyn rodnoi?");
    }
}
