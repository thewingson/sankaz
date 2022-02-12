package kz.open.sankaz.rest;

import kz.open.sankaz.mapper.OrganizationMapper;
import kz.open.sankaz.pojo.filter.OrganizationEditFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/orgs")
public class OrganizationRest {

    private final OrganizationService organizationService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    public OrganizationRest(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/{orgId}")
    public ResponseEntity<?> getOne(@PathVariable Long orgId) {
        try {
            return ResponseModel.success(organizationMapper.organizationToDtoWithAddData(organizationService.getOne(orgId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{orgId}/profile-finish")
    public ResponseEntity<?> profileFinish(@PathVariable Long orgId,
                                           @Valid @RequestBody OrganizationEditFilter filter) {
        try {
            organizationService.finishProfile(orgId, filter);
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
