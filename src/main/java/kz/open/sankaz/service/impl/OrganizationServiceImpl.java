package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.exception.OrganizationRegisterException;
import kz.open.sankaz.exception.OrganizationRegisterExceptionMessages;
import kz.open.sankaz.mapper.OrganizationMapper;
import kz.open.sankaz.model.CompanyCategory;
import kz.open.sankaz.model.Organization;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.pojo.filter.OrganizationCreateFilter;
import kz.open.sankaz.pojo.filter.OrganizationEditFilter;
import kz.open.sankaz.pojo.filter.OrganizationFilterFilter;
import kz.open.sankaz.repo.OrganizationRepo;
import kz.open.sankaz.service.*;
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

    @Autowired
    private UserService userService;

    @Lazy
    @Autowired
    private AuthService authService;

    @Value("${application.file.upload.path.image}")
    private String APPLICATION_UPLOAD_PATH_IMAGE;

    @Value("${application.file.download.path.image}")
    private String APPLICATION_DOWNLOAD_PATH_IMAGE;

    public OrganizationServiceImpl(OrganizationRepo organizationRepo) {
        super(organizationRepo);
        this.organizationRepo = organizationRepo;
    }

    @Override
    public Organization getOrganizationByTelNumber(String telNumber) {
        Optional<Organization> organization = repo.findByTelNumber(telNumber);
        if(!organization.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("telNumber", telNumber);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return organization.get();
    }

    @Override
    public Organization getOrganizationByUser(SecUser user) {
        Optional<Organization> organization = organizationRepo.findByUser(user);
        if(!organization.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("user", user.getUsername());
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return organization.get();
    }

    @Override
    public Organization getOrganizationByIban(String iban) {
        Optional<Organization> organization = repo.findByIban(iban);
        if(!organization.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("iban", iban);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return organization.get();
    }

    @Override
    public Organization getOrganizationByIin(String iin) {
        Optional<Organization> organization = repo.findByIin(iin);
        if(!organization.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("iin", iin);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return organization.get();
    }

    @Override
    public List<Organization> getAllByConfirmationStatuses(OrganizationFilterFilter filter) {
        return organizationRepo.findAllByConfirmationStatusIn(filter.getConfirmationStatuses());
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
        organization.setConfirmedDate(LocalDateTime.now());
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
        organization.setConfirmedDate(LocalDateTime.now());
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
            File uploadDir = new File(APPLICATION_UPLOAD_PATH_IMAGE);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + pic.getOriginalFilename();
            fileNameWithPath = APPLICATION_UPLOAD_PATH_IMAGE + "/" + resultFilename;

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
                File uploadDir = new File(APPLICATION_UPLOAD_PATH_IMAGE);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + pic.getOriginalFilename();
                String fileNameWithPath = APPLICATION_UPLOAD_PATH_IMAGE + "/" + resultFilename;

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
    public Organization createOrg(OrganizationCreateFilter filter) {
        SecUser user = userService.getOne(filter.getUserId());
        if(!user.getUserType().equals("ORG")){
            throw new RuntimeException("Пользователь не является организацией!");
        }

        CompanyCategory companyCategory = companyCategoryService.getOne(filter.getCompanyCategoryId());

        try{ // проверка email
            userService.getUserByEmail(filter.getEmail());
            log.warn("Email is busy {}", filter.getEmail());
            throw new OrganizationRegisterException(OrganizationRegisterExceptionMessages.EMAIL_IS_BUSY_CODE);
        } catch (EntityNotFoundException e){
            log.info("Email is free {}", filter.getTelNumber());
        }

        try{ // проверка iban
            getOrganizationByIban(filter.getIban());
            log.warn("IBAN is busy {}", filter.getIban());
            throw new OrganizationRegisterException(OrganizationRegisterExceptionMessages.IBAN_IS_BUSY_CODE);
        } catch (EntityNotFoundException e){
            log.info("IBAN is free {}", filter.getTelNumber());
        }

        try{ // проверка iin
            getOrganizationByIin(filter.getIin());
            log.warn("IIN is busy {}", filter.getIban());
            throw new OrganizationRegisterException(OrganizationRegisterExceptionMessages.IIN_IS_BUSY_CODE);
        } catch (EntityNotFoundException e){
            log.info("IIN is free {}", filter.getTelNumber());
        }

        try{ // проверка номера в организациях
            getOrganizationByTelNumber(filter.getTelNumber());
            log.warn("Number is busy {}", filter.getTelNumber());
            throw new OrganizationRegisterException(OrganizationRegisterExceptionMessages.TEL_NUMBER_IS_BUSY_CODE);
        } catch (EntityNotFoundException e){
            log.info("Number is free {}", filter.getTelNumber());
        }
        try{ // проверка организации по юзеру / один юзер - одна организация !
            getOrganizationByUser(user);
            log.warn("User already has registered organization {}", user.getTelNumber());
            throw new OrganizationRegisterException(OrganizationRegisterExceptionMessages.ORG_ALREADY_REGISTERED_CODE);
        } catch (EntityNotFoundException e){
            log.info("User did not registered any organization {}", user.getTelNumber());
        }

        Organization organization = new Organization();
        organization.setUser(user);
        organization.setConfirmationStatus("CONFIRMED");
        organization.setEmail(filter.getEmail());
        organization.setIban(filter.getIban());
        organization.setName(filter.getName());
        organization.setManagerFullName(filter.getManagerFullName());
        organization.setTelNumber(filter.getTelNumber());
        organization.setIin(filter.getIin());
        organization.setCompanyCategory(companyCategory);
        organization.setCompanyName(filter.getCompanyName());
        organization.setAddress(filter.getAddress());
        organization.setDescription(filter.getDescription());
        organization.setSiteLink(filter.getSiteLink());
        organization.setInstagramLink(filter.getInstagramLink());
        addOne(organization);
        return organization;
    }

    @Override
    public Organization editOrg(Long orgId, OrganizationCreateFilter filter) {
        Organization organization = getOne(orgId);
        SecUser user = userService.getOne(filter.getUserId());
        if(!user.getUserType().equals("ORG")){
            throw new RuntimeException("Пользователь не является организацией!");
        }

        CompanyCategory companyCategory = companyCategoryService.getOne(filter.getCompanyCategoryId());

        try{ // проверка email
            SecUser userByEmail = userService.getUserByEmail(filter.getEmail());
            if(!organization.getUser().equals(userByEmail)){
                log.warn("Email is busy {}", filter.getEmail());
                throw new OrganizationRegisterException(OrganizationRegisterExceptionMessages.EMAIL_IS_BUSY_CODE);
            }
        } catch (EntityNotFoundException e){
            log.info("Email is free {}", filter.getTelNumber());
        }

        try{ // проверка iban
            Organization organizationByIban = getOrganizationByIban(filter.getIban());
            if(!organization.getId().equals(organizationByIban.getId())){
                log.warn("IBAN is busy {}", filter.getIban());
                throw new OrganizationRegisterException(OrganizationRegisterExceptionMessages.IBAN_IS_BUSY_CODE);
            }
        } catch (EntityNotFoundException e){
            log.info("IBAN is free {}", filter.getTelNumber());
        }

        try{ // проверка iin
            Organization organizationByIin = getOrganizationByIin(filter.getIin());
            if(!organization.getId().equals(organizationByIin.getId())){
                log.warn("IIN is busy {}", filter.getIban());
                throw new OrganizationRegisterException(OrganizationRegisterExceptionMessages.IIN_IS_BUSY_CODE);
            }
        } catch (EntityNotFoundException e){
            log.info("IIN is free {}", filter.getTelNumber());
        }

        try{ // проверка номера в организациях
            Organization organizationByTelNumber = getOrganizationByTelNumber(filter.getTelNumber());
            if(!organization.getId().equals(organizationByTelNumber.getId())){
                log.warn("Number is busy {}", filter.getTelNumber());
                throw new OrganizationRegisterException(OrganizationRegisterExceptionMessages.TEL_NUMBER_IS_BUSY_CODE);
            }
        } catch (EntityNotFoundException e){
            log.info("Number is free {}", filter.getTelNumber());
        }
        try{ // проверка организации по юзеру / один юзер - одна организация !
            Organization organizationByUser = getOrganizationByUser(user);
            if(!organization.getId().equals(organizationByUser.getId())){
                log.warn("User already has registered organization {}", user.getTelNumber());
                throw new OrganizationRegisterException(OrganizationRegisterExceptionMessages.ORG_ALREADY_REGISTERED_CODE);
            }
        } catch (EntityNotFoundException e){
            log.info("User did not registered any organization {}", user.getTelNumber());
        }

        organization.setUser(user);
        organization.setEmail(filter.getEmail());
        organization.setIban(filter.getIban());
        organization.setName(filter.getName());
        organization.setManagerFullName(filter.getManagerFullName());
        organization.setTelNumber(filter.getTelNumber());
        organization.setIin(filter.getIin());
        organization.setCompanyCategory(companyCategory);
        organization.setCompanyName(filter.getCompanyName());
        organization.setAddress(filter.getAddress());
        organization.setDescription(filter.getDescription());
        organization.setSiteLink(filter.getSiteLink());
        organization.setInstagramLink(filter.getInstagramLink());
        editOneById(organization);
        return organization;
    }

    @Override
    public boolean checkIfOwnOrg(Long orgId) {
        String currentUsername = authService.getCurrentUsername();
        Organization currentUsersOrganization = getOrganizationByTelNumber(currentUsername);
        if(!orgId.equals(currentUsersOrganization.getId())){
            throw new RuntimeException("Данная организация не является вашей!");
        }
        return true;
    }

    @Override
    protected Class getCurrentClass() {
        return Organization.class;
    }
}
