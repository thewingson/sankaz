package kz.open.sankaz.service;

import kz.open.sankaz.model.SecUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface AuthService {
    SecUser getCurrentUser();
    void confirmAccount(String tokenId);
    void registerUser(SecUser user);
    Map<String, Object> refreshToken(HttpServletRequest request, HttpServletResponse response);
    void signOut(String username);
}
