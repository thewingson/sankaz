package kz.open.sankaz.rest;

import kz.open.sankaz.pojo.dto.*;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/orgs")
public class OrganizationRest {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationRest(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/{orgId}")
    public ResponseEntity<?> getOne(@PathVariable Long orgId) {
        try {
            return ResponseModel.success(organizationService.getOneDto(orgId));
        } catch (RuntimeException e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{orgId}/profile-finish")
    public ResponseEntity<?> profileFinish(@PathVariable Long orgId,
                                           @RequestBody OrganizationAddDataDto organizationAddDataDto) {
        try {
            organizationService.finishProfile(orgId, organizationAddDataDto);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{orgId}/upload-pic")
    public ResponseEntity<?> uploadPicture(@PathVariable Long orgId,
                                            @RequestParam("pic") MultipartFile pic) {
        try {
            organizationService.uploadPicture(orgId, pic);
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{orgId}/upload-pic/list")
    public ResponseEntity<?> uploadPictures(@PathVariable Long orgId,
                                           @RequestParam("pics") MultipartFile[] pics) {
        try {
            organizationService.uploadPicture(orgId, pics);
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

}
