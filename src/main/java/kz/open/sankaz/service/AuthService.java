package kz.open.sankaz.service;

import kz.open.sankaz.pojo.dto.ConfirmationStatusDto;
import kz.open.sankaz.pojo.dto.NumberFreeDto;
import kz.open.sankaz.pojo.dto.TokenDto;
import kz.open.sankaz.pojo.filter.FinishRegFilter;
import kz.open.sankaz.pojo.filter.OrganizationRegisterFinishFilter;
import kz.open.sankaz.pojo.filter.RegisterFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface AuthService {
    String getCurrentUsername();

    Map<String, Object> refreshToken(HttpServletRequest request, HttpServletResponse response);

    List<String> getNumbers();

    int sendConfirmationNumber(String telNumber);

    int sendConfirmationNumberOrganization(String telNumber, String password, String confirmPassword);

    void checkConfirmationNumber(RegisterFilter filter);

    TokenDto checkConfirmationNumberOrganization(String telNumber, String password, int confirmNumber);

    TokenDto finishRegistration(FinishRegFilter filter);

    void registerOrganization(OrganizationRegisterFinishFilter filter);

    int sendResetNumber(String telNumber);

    TokenDto resetPassword(String telNumber, String password, String confirmPassword);

    void checkResetNumber(String telNumber, int resetNumber);

    NumberFreeDto isNumberFree(String telNumber);

    ConfirmationStatusDto getOrganizationConfirmationStatus(String telNumber);
}
