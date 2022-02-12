package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.mapper.OrganizationMapper;
import kz.open.sankaz.model.CompanyCategory;
import kz.open.sankaz.model.Organization;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.pojo.filter.OrganizationEditFilter;
import kz.open.sankaz.pojo.filter.OrganizationFilterFilter;
import kz.open.sankaz.repo.OrganizationRepo;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.CompanyCategoryService;
import kz.open.sankaz.service.OrganizationService;
import kz.open.sankaz.service.SysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@Transactional
public class OrganizationServiceImpl extends AbstractService<Organization, OrganizationRepo> implements OrganizationService {

    private final OrganizationRepo organizationRepo;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private CompanyCategoryService companyCategoryService;

    @Autowired
    private SysFileService fileService;

    @Lazy
    @Autowired
    private AuthService authService;

    @Value("${application.file.upload.path}")
    private String APPLICATION_UPLOAD_PATH;

    public OrganizationServiceImpl(OrganizationRepo organizationRepo) {
        super(organizationRepo);
        this.organizationRepo = organizationRepo;
    }

    @Override
    public Organization getOrganizationByTelNumber(String telNumber) {
        Optional<Organization> organization = repo.findByTelNumberAndDeletedByIsNull(telNumber);
        if(!organization.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("telNumber", telNumber);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return organization.get();
    }

    @Override
    public Organization getOrganizationByUser(SecUser user) {
        Optional<Organization> organization = organizationRepo.findByUserAndDeletedByIsNull(user);
        if(!organization.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("user", user.getUsername());
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return organization.get();
    }

    @Override
    public Organization getOrganizationByIban(String iban) {
        Optional<Organization> organization = repo.findByIbanAndDeletedByIsNull(iban);
        if(!organization.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("iban", iban);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return organization.get();
    }

    @Override
    public Organization getOrganizationByIin(String iin) {
        Optional<Organization> organization = repo.findByIinAndDeletedByIsNull(iin);
        if(!organization.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("iin", iin);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return organization.get();
    }

    @Override
    public List<Organization> getAllByConfirmationStatuses(OrganizationFilterFilter filter) {
        return organizationRepo.findAllByConfirmationStatusInAndDeletedByIsNull(filter.getConfirmationStatuses());
    }

    @Override
    public void approveOrganizationData(Long orgId) {
        log.info("OrganizationServiceImpl. Starting approve organization id: {}", orgId);
        Organization organization = getOne(orgId);
        if(organization.getConfirmationStatus().equals("CONFIRMED")){
            log.warn("OrganizationServiceImpl. Organization {} already has been confirmed", orgId);
            throw new RuntimeException("Данная организация уже одобрена!");
        }
        organization.setConfirmationStatus("CONFIRMED");
        organization.setConfirmedTs(LocalDateTime.now());
        organization.setConfirmedBy(authService.getCurrentUsername());
        log.info("OrganizationServiceImpl. Organization {} confirmed successfully", orgId);
    }

    @Override
    public void rejectOrganizationData(Long orgId) {
        log.info("OrganizationServiceImpl. Starting reject organization id: {}", orgId);
        Organization organization = getOne(orgId);
        if(organization.getConfirmationStatus().equals("REJECTED")){
            log.warn("OrganizationServiceImpl. Organization {} already has been rejected", orgId);
            throw new RuntimeException("Данная организация уже отклонена!");
        }
        organization.setConfirmationStatus("REJECTED");
        organization.setConfirmedTs(LocalDateTime.now());
        organization.setConfirmedBy(authService.getCurrentUsername());
        log.info("OrganizationServiceImpl. Organization {} rejected successfully", orgId);
    }

    @Override
    public void finishProfile(Long orgId, OrganizationEditFilter filter) {
        log.info("OrganizationServiceImpl. Starting update organization id: {}", orgId);
        Organization organization = getOne(orgId);
        if(!organization.getConfirmationStatus().equals("CONFIRMED")){
            log.warn("OrganizationServiceImpl. Organization {} has not confirmed", orgId);
            throw new RuntimeException("Данная организация еще не одобрена!");
        }
        CompanyCategory category = companyCategoryService.getOne(filter.getCategoryId());

        organization.setCompanyCategory(category);
        organization.setCompanyCategory(category);
        organization.setCompanyName(filter.getCompanyName());
        organization.setDescription(filter.getDescription());
        organization.setAddress(filter.getAddress());
        organization.setInstagramLink(filter.getInstagramLink());
        organization.setSiteLink(filter.getSiteLink());

        editOneById(organization);
        log.info("OrganizationServiceImpl. Organization {} update has completed successfully", orgId);
    }

    @Override
    public void uploadPicture(Long orgId, MultipartFile pic) throws IOException {
        Organization organization = getOne(orgId);
        String fileNameWithPath = "";
        if (pic != null && !pic.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(APPLICATION_UPLOAD_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + pic.getOriginalFilename();
            fileNameWithPath = APPLICATION_UPLOAD_PATH + "/" + resultFilename;

            pic.transferTo(new File(fileNameWithPath));

            SysFile file = new SysFile();
            file.setFileName(resultFilename);
            file.setExtension(pic.getContentType());
            file.setSize(pic.getSize());
            file = fileService.addOne(file);

            organization.addPic(file);
            editOneById(organization);
        }
    }

    @Override
    public void uploadPicture(Long orgId, MultipartFile[] pics) throws IOException {
        Organization organization = getOne(orgId);
        for(MultipartFile pic : pics){
            if (!pic.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(APPLICATION_UPLOAD_PATH);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + pic.getOriginalFilename();
                String fileNameWithPath = APPLICATION_UPLOAD_PATH + "/" + resultFilename;

                pic.transferTo(new File(fileNameWithPath));

                SysFile file = new SysFile();
                file.setFileName(resultFilename);
                file.setExtension(pic.getContentType());
                file.setSize(pic.getSize());
                file = fileService.addOne(file);

                organization.addPic(file);

            }
        }
        editOneById(organization);
    }

    @Override
    protected Class getCurrentClass() {
        return Organization.class;
    }
}
