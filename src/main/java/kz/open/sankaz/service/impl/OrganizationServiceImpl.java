package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.exception.OrganizationRegisterException;
import kz.open.sankaz.exception.OrganizationRegisterExceptionMessages;
import kz.open.sankaz.mapper.OrganizationMapper;
import kz.open.sankaz.model.CompanyCategory;
import kz.open.sankaz.model.Organization;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.model.enums.OrganizationConfirmationStatus;
import kz.open.sankaz.model.enums.UserType;
import kz.open.sankaz.pojo.dto.PageDto;
import kz.open.sankaz.pojo.filter.OrganizationCreateFilter;
import kz.open.sankaz.pojo.filter.OrganizationEditFilter;
import kz.open.sankaz.pojo.filter.OrganizationFilterFilter;
import kz.open.sankaz.repo.OrganizationRepo;
import kz.open.sankaz.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Transactional
@Service
public class OrganizationServiceImpl extends AbstractService<Organization, OrganizationRepo> implements OrganizationService {

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

    @Autowired
    private SysFileService sysFileService;

    @Value("${application.file.upload.path.image}")
    private String APPLICATION_UPLOAD_PATH_IMAGE;

    @Value("${application.file.download.path.image}")
    private String APPLICATION_DOWNLOAD_PATH_IMAGE;

    public OrganizationServiceImpl(OrganizationRepo organizationRepo) {
        super(organizationRepo);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public Organization getOrganizationByUser(SecUser user) {
        Optional<Organization> organization = repo.findByUser(user);
        if(!organization.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("user", user.getUsername());
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return organization.get();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
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
        return repo.findAllByConfirmationStatusIn(filter.getConfirmationStatuses());
    }

    @Override
    public void approveOrganizationData(Long orgId) {
        Organization organization = getOne(orgId);
        if(organization.getConfirmationStatus().equals(OrganizationConfirmationStatus.CONFIRMED)){
            throw new RuntimeException("???????????? ?????????????????????? ?????? ????????????????!");
        }
        organization.setRejectedDate(null);
        organization.setRejectMessage(null);
        organization.setApprovedDate(LocalDateTime.now());
        organization.setConfirmationStatus(OrganizationConfirmationStatus.CONFIRMED);
        organization.setConfirmedBy(authService.getCurrentUsername());
        editOneById(organization);
    }

    @Override
    public void rejectOrganizationData(Long orgId, String rejectMessage) {
        Organization organization = getOne(orgId);
        if(organization.getConfirmationStatus().equals(OrganizationConfirmationStatus.REJECTED)){
            throw new RuntimeException("???????????? ?????????????????????? ?????? ??????????????????!");
        }
        if(!organization.getConfirmationStatus().equals(OrganizationConfirmationStatus.ON_CONFIRMATION)){
            throw new RuntimeException("?????????????????????? ???? ?????????????????? ???? ??????????????????????!");
        }
        organization.setApprovedDate(null);
        organization.setRejectedDate(LocalDateTime.now());
        organization.setRejectMessage(rejectMessage);
        organization.setConfirmationStatus(OrganizationConfirmationStatus.REJECTED);
        organization.setConfirmedBy(authService.getCurrentUsername());
        editOneById(organization);
    }

    @Override
    public void finishProfile(Long orgId, OrganizationEditFilter filter) {
        Organization organization = getOne(orgId);
        if(!organization.getConfirmationStatus().equals(OrganizationConfirmationStatus.CONFIRMED)){
            throw new RuntimeException("???????????? ?????????????????????? ?????? ???? ????????????????!");
        }
        CompanyCategory category = companyCategoryService.getOne(filter.getCategoryId());

        organization.setConfirmationStatus(OrganizationConfirmationStatus.PROFILE_FINISHED);
        organization.setCompanyCategory(category);
        organization.setCompanyCategory(category);
        organization.setDescription(filter.getDescription());
        organization.setAddress(filter.getAddress());
        organization.setInstagramLink(filter.getInstagramLink());
        organization.setSiteLink(filter.getSiteLink());

        editOneById(organization);
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
        if(!user.getUserType().equals(UserType.ORG)){
            throw new RuntimeException("???????????????????????? ???? ???????????????? ????????????????????????!");
        }

        CompanyCategory companyCategory = companyCategoryService.getOne(filter.getCompanyCategoryId());

        try{
            userService.getUserByEmail(filter.getEmail());
            throw new RuntimeException("IBAN ?????? ??????????????????????????????");
        } catch (EntityNotFoundException e){
        }

        try{
            getOrganizationByIban(filter.getIban());
            throw new RuntimeException("IBAN ?????? ??????????????????????????????");
        } catch (EntityNotFoundException e){
        }

        try{
            getOrganizationByIin(filter.getIin());
            throw new RuntimeException("IIN ?????? ??????????????????????????????");
        } catch (EntityNotFoundException e){
        }

        try{ // ???????????????? ???????????? ?? ????????????????????????
            getOrganizationByTelNumber(filter.getTelNumber());
            throw new RuntimeException("?????????? ???????????????? ?????? ??????????????????????????????");
        } catch (EntityNotFoundException e){
        }
        try{ // ???????????????? ?????????????????????? ???? ?????????? / ???????? ???????? - ???????? ?????????????????????? !
            getOrganizationByUser(user);
            throw new RuntimeException("???? ?????????????? ???????????? ?????? ???????????????????????????????? ??????????????????????");
        } catch (EntityNotFoundException e){
        }

        Organization organization = new Organization();
        organization.setUser(user);
        organization.setConfirmationStatus(OrganizationConfirmationStatus.valueOf(filter.getConfirmationStatus()));
        organization.setEmail(filter.getEmail());
        organization.setIban(filter.getIban());
        organization.setName(filter.getName());
        organization.setManagerFullName(filter.getManagerFullName());
        organization.setTelNumber(filter.getTelNumber());
        organization.setIin(filter.getIin());
        organization.setCompanyCategory(companyCategory);
        organization.setAddress(filter.getAddress());
        organization.setDescription(filter.getDescription());
        organization.setSiteLink(filter.getSiteLink());
        organization.setInstagramLink(filter.getInstagramLink());
        return repo.save(organization);
    }

    @Override
    public Organization editOrg(Long orgId, OrganizationCreateFilter filter) {
        Organization organization = getOne(orgId);
        SecUser user = userService.getOne(filter.getUserId());
        if(!user.getUserType().equals(UserType.ORG)){
            throw new RuntimeException("???????????????????????? ???? ???????????????? ????????????????????????!");
        }

        CompanyCategory companyCategory = companyCategoryService.getOne(filter.getCompanyCategoryId());

        try{ // ???????????????? email
            SecUser userByEmail = userService.getUserByEmail(filter.getEmail());
            if(!organization.getUser().equals(userByEmail)){
                throw new OrganizationRegisterException(OrganizationRegisterExceptionMessages.EMAIL_IS_BUSY_CODE);
            }
        } catch (EntityNotFoundException e){
        }

        try{ // ???????????????? iban
            Organization organizationByIban = getOrganizationByIban(filter.getIban());
            if(!organization.getId().equals(organizationByIban.getId())){
                throw new OrganizationRegisterException(OrganizationRegisterExceptionMessages.IBAN_IS_BUSY_CODE);
            }
        } catch (EntityNotFoundException e){
        }

        try{ // ???????????????? iin
            Organization organizationByIin = getOrganizationByIin(filter.getIin());
            if(!organization.getId().equals(organizationByIin.getId())){
                throw new OrganizationRegisterException(OrganizationRegisterExceptionMessages.IIN_IS_BUSY_CODE);
            }
        } catch (EntityNotFoundException e){
        }

        try{ // ???????????????? ???????????? ?? ????????????????????????
            Organization organizationByTelNumber = getOrganizationByTelNumber(filter.getTelNumber());
            if(!organization.getId().equals(organizationByTelNumber.getId())){
                throw new OrganizationRegisterException(OrganizationRegisterExceptionMessages.TEL_NUMBER_IS_BUSY_CODE);
            }
        } catch (EntityNotFoundException e){
        }
        try{ // ???????????????? ?????????????????????? ???? ?????????? / ???????? ???????? - ???????? ?????????????????????? !
            Organization organizationByUser = getOrganizationByUser(user);
            if(!organization.getId().equals(organizationByUser.getId())){
                throw new OrganizationRegisterException(OrganizationRegisterExceptionMessages.ORG_ALREADY_REGISTERED_CODE);
            }
        } catch (EntityNotFoundException e){
        }

        organization.setUser(user);
        organization.setEmail(filter.getEmail());
        organization.setIban(filter.getIban());
        organization.setName(filter.getName());
        organization.setManagerFullName(filter.getManagerFullName());
        organization.setTelNumber(filter.getTelNumber());
        organization.setIin(filter.getIin());
        organization.setCompanyCategory(companyCategory);
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
            throw new RuntimeException("???????????? ?????????????????????? ???? ???????????????? ??????????!");
        }
        return true;
    }

    @Override
    public PageDto getAllFilters(String name, String address, String companyCategoryCode, String confirmationStatus, int page, int size) {
        Page<Organization> pages = repo.findAllByFilters(
                name.toLowerCase(),
                address.toLowerCase(),
                companyCategoryCode.toLowerCase(),
                confirmationStatus.toLowerCase(),
                PageRequest.of(page, size));
        PageDto dto = new PageDto();
        dto.setTotal(pages.getTotalElements());
        dto.setContent(organizationMapper.organizationToDto(pages.getContent()));
        dto.setPageable(pages.getPageable());
        return dto;
    }

    @Override
    public List<SysFile> addPics(Long orgId, MultipartFile[] pics) throws IOException {
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
                file = sysFileService.addOne(file);

                organization.addPic(file);
            }
        }

        return editOneById(organization).getPics();
    }

    @Override
    public void deletePics(Long orgId, Long picId) {
        Organization organization = getOne(orgId);

        SysFile picToDelete = sysFileService.getOne(picId);
        picToDelete.setDeletedDate(LocalDate.now());
        sysFileService.editOneById(picToDelete);
        organization.deletePic(picToDelete);
        editOneById(organization);
    }

    @Override
    protected Class getCurrentClass() {
        return Organization.class;
    }
}
