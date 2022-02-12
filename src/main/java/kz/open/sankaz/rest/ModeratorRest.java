package kz.open.sankaz.rest;

import kz.open.sankaz.mapper.OrganizationMapper;
import kz.open.sankaz.pojo.filter.OrganizationFilterFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/moders")
public class ModeratorRest {

    private final OrganizationService organizationService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    public ModeratorRest(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/orgs")
    public ResponseEntity<?> getAllOrganizations(@Valid @RequestBody OrganizationFilterFilter filter) {
        try {
            return ResponseModel.success(organizationMapper.organizationToDto(organizationService.getAllByConfirmationStatuses(filter)));
        } catch (RuntimeException e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/orgs/{orgId}")
    public ResponseEntity<?> getAllOrganizations(@PathVariable("orgId") Long orgId) {
        try {
            return ResponseModel.success(organizationMapper.organizationToDtoWithAddData(organizationService.getOne(orgId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/orgs/{orgId}/approve")
    public ResponseEntity<?> approveOrganizationData(@PathVariable("orgId") Long orgId) {
        try {
            organizationService.approveOrganizationData(orgId);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/orgs/{orgId}/reject")
    public ResponseEntity<?> rejectOrganizationData(@PathVariable("orgId") Long orgId) {
        try {
            organizationService.rejectOrganizationData(orgId);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
