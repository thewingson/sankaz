package kz.open.sankaz.service;

import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.model.auth.AuthRequest;

public interface AuthService {
    void signOut(AuthRequest authRequest);
    SecUser getCurrentUser();
    void confirmAccount(String tokenId);
}
