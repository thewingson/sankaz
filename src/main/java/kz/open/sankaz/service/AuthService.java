package kz.open.sankaz.service;

import kz.open.sankaz.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface AuthService {
//    SecUser getCurrentUser();
    String getCurrentUsername();

//    void confirmAccount(String tokenId);
//    void registerUser(SecUserDto userDto);
    Map<String, Object> refreshToken(HttpServletRequest request, HttpServletResponse response);
//    void signOut(String username);

    List<String> getNumbers();

    int sendConfirmationNumber(NumberDto numberDto);

    void checkConfirmationNumber(RegisterDto registerDto);

    SecUserDto finishRegistration(FinishRegDto finishRegDto);

    int sendResetNumber(NumberDto numberDto);

    SecUserDto resetPassword(ResetPasswordDto resetPasswordDto);

    void checkResetNumber(RegisterDto registerDto);
}
