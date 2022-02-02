package kz.open.sankaz.rest;

import kz.open.sankaz.dto.*;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/orgs/auth")
public class OrganizationAuthRest {

    private final AuthService authService;

    @Autowired
    public OrganizationAuthRest(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/numbers/is-free")
    public ResponseEntity<?> isNumberFree(@RequestBody NumberDto numberDto) {
        try {
            return ResponseModel.success(authService.isNumberFree(numberDto.getTelNumber()));
        } catch (RuntimeException e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/send-conf")
    public ResponseEntity<?> sendConfirmationNumber(@RequestBody ResetPasswordDto resetPasswordDto) {
        try {
            authService.sendConfirmationNumberOrganization(resetPasswordDto);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/check-conf")
    public ResponseEntity<?> checkConfirmationNumber(@RequestBody RegisterOrganizationDto registerDto) {
        try {
            return ResponseModel.success(authService.checkConfirmationNumberOrganization(registerDto));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/register-org")
    public ResponseEntity<?> registerOrganization(@RequestBody RegisterOrgDto registerOrgDto) {
        try {
            authService.registerOrganization(registerOrgDto);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/check-status")
    public ResponseEntity<?> getOrganizationConfirmationStatus(@RequestBody NumberDto numberDto) {
        try {
            return ResponseModel.success(authService.getOrganizationConfirmationStatus(numberDto));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/send-reset")
    public ResponseEntity<?> sedResetNumber(@RequestBody NumberDto numberDto) {
        try {
            authService.sendResetNumber(numberDto);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/check-reset")
    public ResponseEntity<?> checkResetNumber(@RequestBody RegisterDto registerDto) {
        try {
            authService.checkResetNumber(registerDto);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/reset-pass")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        try {
            return ResponseModel.success(authService.resetPassword(resetPasswordDto));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

}
