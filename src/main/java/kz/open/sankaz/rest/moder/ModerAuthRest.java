package kz.open.sankaz.rest.moder;

import kz.open.sankaz.mapper.OrganizationMapper;
import kz.open.sankaz.pojo.filter.*;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_MODERATOR')")
@RestController
@RequestMapping("/moders/auth")
public class ModerAuthRest {

    private final AuthService authService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    public ModerAuthRest(AuthService authService) {
        this.authService = authService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/numbers/is-free")
    public ResponseEntity<?> isNumberFree(@Valid @RequestBody TelNumberFilter filter) {
        try {
            return ResponseModel.success(authService.isNumberFree(filter.getTelNumber()));
        } catch (RuntimeException e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/send-conf")
    public ResponseEntity<?> sendConfirmationNumber(@Valid @RequestBody OrganizationConfirmationNumberFilter filter) {
        try {
            authService.sendConfirmationNumberOrganization(filter.getTelNumber(), filter.getPassword(), filter.getConfirmPassword());
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/check-conf")
    public ResponseEntity<?> checkConfirmationNumber(@Valid @RequestBody OrganizationRegisterFilter filter) {
        try {
            return ResponseModel.success(authService.checkConfirmationNumberOrganization(filter.getTelNumber(), filter.getPassword(), filter.getConfirmationNumber()));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/register-org")
    public ResponseEntity<?> registerOrganization(@RequestBody OrganizationRegisterFinishFilter filter) {
        try {
            return ResponseModel.success(organizationMapper.toOrganizationRegisterDto(authService.registerOrganization(filter)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/check-status")
    public ResponseEntity<?> getOrganizationConfirmationStatus(@Valid @RequestBody TelNumberFilter filter) {
        try {
            return ResponseModel.success(authService.getOrganizationConfirmationStatus(filter.getTelNumber()));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/profile-finish/{orgId}")
    public ResponseEntity<?> profileFinish(@PathVariable Long orgId,
                                           @Valid @RequestBody OrganizationEditFilter filter) {
        try {
            organizationService.checkIfOwnOrg(orgId);
            organizationService.finishProfile(orgId, filter);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getOwnProfile(HttpServletRequest request) {
        try {
            return ResponseModel.success(organizationMapper.toOrganizationRegisterDto(authService.getOwnProfile(request)));
        } catch (RuntimeException e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/profile/{orgId}")
    public ResponseEntity<?> editOrg(@PathVariable(name = "orgId") Long orgId,
                                         @Valid @RequestBody OrganizationCreateFilter filter) {
        try{
            organizationService.checkIfOwnOrg(orgId);
            return ResponseModel.success(organizationMapper.organizationToDto(organizationService.editOrg(orgId, filter)));
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
//            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/upload-pic/{orgId}/list")
    public ResponseEntity<?> uploadPictures(@PathVariable Long orgId,
                                            @RequestParam("pics") MultipartFile[] pics) {
        try {
            organizationService.checkIfOwnOrg(orgId);
            organizationService.uploadPicture(orgId, pics);
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/send-reset")
    public ResponseEntity<?> sedResetNumber(@Valid @RequestBody TelNumberFilter filter) {
        try {
            authService.sendResetNumber(filter.getTelNumber());
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/check-reset")
    public ResponseEntity<?> checkResetNumber(@Valid @RequestBody RegisterFilter registerFilter) {
        try {
            authService.checkResetNumber(registerFilter.getTelNumber(), registerFilter.getResetNumber());
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/reset-pass")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordFilter filter) {
        try {
            return ResponseModel.success(authService.resetPassword(filter.getTelNumber(), filter.getPassword(), filter.getConfirmPassword()));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

}
