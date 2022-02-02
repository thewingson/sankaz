package kz.open.sankaz.service;

import kz.open.sankaz.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface AuthService {
    String getCurrentUsername();

    Map<String, Object> refreshToken(HttpServletRequest request, HttpServletResponse response);

    List<String> getNumbers();

    int sendConfirmationNumber(NumberDto numberDto);

    int sendConfirmationNumberOrganization(ResetPasswordDto resetPasswordDto);

    void checkConfirmationNumber(RegisterDto registerDto);

    TokenDto checkConfirmationNumberOrganization(RegisterOrganizationDto registerDto);

    TokenDto finishRegistration(FinishRegDto finishRegDto);

    void registerOrganization(RegisterOrgDto registerOrgDto);

    int sendResetNumber(NumberDto numberDto);

    TokenDto resetPassword(ResetPasswordDto resetPasswordDto);

    void checkResetNumber(RegisterDto registerDto);

    NumberFreeDto isNumberFree(String telNumber);

    ConfirmationStatusDto getOrganizationConfirmationStatus(NumberDto numberDto);
}
