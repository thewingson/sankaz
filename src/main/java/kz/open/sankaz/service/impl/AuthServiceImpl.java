package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.model.auth.AuthRequest;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @Override
    public void signOut(AuthRequest authRequest) {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    }

    @Override
    public SecUser getCurrentUser() {
        return (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
