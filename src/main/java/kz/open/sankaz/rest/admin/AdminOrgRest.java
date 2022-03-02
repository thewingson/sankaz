package kz.open.sankaz.rest.admin;

import kz.open.sankaz.mapper.OrganizationMapper;
import kz.open.sankaz.pojo.filter.OrganizationCreateFilter;
import kz.open.sankaz.pojo.filter.OrganizationFilterFilter;
import kz.open.sankaz.pojo.filter.RejectMessageFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/orgs")
public class AdminOrgRest {

    private final OrganizationService organizationService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    public AdminOrgRest(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try{
            return ResponseModel.success(organizationMapper.organizationToDto(organizationService.getAll()));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getAllOrganizations(@Valid @RequestBody OrganizationFilterFilter filter) {
        try {
            return ResponseModel.success(organizationMapper.organizationToDto(organizationService.getAllByConfirmationStatuses(filter)));
        } catch (RuntimeException e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{orgId}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "orgId") Long orgId) {
        try{
            return ResponseModel.success(organizationMapper.organizationToDto(organizationService.getOne(orgId)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@Valid @RequestBody OrganizationCreateFilter filter) {
        try{
            return ResponseModel.success(organizationMapper.organizationToDto(organizationService.createOrg(filter)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{orgId}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "orgId") Long orgId,
                                         @Valid @RequestBody OrganizationCreateFilter filter) {
        try{
            ;
            return ResponseModel.success(organizationMapper.organizationToDto(organizationService.editOrg(orgId, filter)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{orgId}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "orgId") Long orgId) {
        try{
            organizationService.deleteOneById(orgId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{orgId}/approve")
    public ResponseEntity<?> approveOrganizationData(@PathVariable("orgId") Long orgId) {
        try {
            organizationService.approveOrganizationData(orgId);
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{orgId}/reject")
    public ResponseEntity<?> rejectOrganizationData(@PathVariable("orgId") Long orgId,
                                                    @Valid @RequestBody RejectMessageFilter filter) {
        try {
            organizationService.rejectOrganizationData(orgId, filter.getRejectMessage());
            return ResponseModel.successPure();
        } catch (RuntimeException e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
