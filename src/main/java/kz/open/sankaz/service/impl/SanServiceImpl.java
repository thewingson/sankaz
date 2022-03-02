package kz.open.sankaz.service.impl;

import kz.open.sankaz.mapper.FileMapper;
import kz.open.sankaz.mapper.ReviewMapper;
import kz.open.sankaz.mapper.SanMapper;
import kz.open.sankaz.model.*;
import kz.open.sankaz.model.enums.OrganizationConfirmationStatus;
import kz.open.sankaz.pojo.dto.SanForMainDto;
import kz.open.sankaz.pojo.filter.ReviewCreateFilter;
import kz.open.sankaz.pojo.filter.SanCreateFilter;
import kz.open.sankaz.pojo.filter.SanForMainFilter;
import kz.open.sankaz.repo.SanRepo;
import kz.open.sankaz.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SanServiceImpl extends AbstractService<San, SanRepo> implements SanService {

    private final SanRepo sanRepo;

    @Lazy
    @Autowired
    private SanTypeService sanTypeService;

    @Lazy
    @Autowired
    private UserService userService;

    @Lazy
    @Autowired
    private AuthService authService;

    @Autowired
    private TelNumberService telNumberService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    protected OrganizationService organizationService;

    @Lazy
    @Autowired
    private RoomService roomService;

    @Autowired
    private CityService cityService;

    @Autowired
    private SanMapper sanMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private FileMapper fileMapper;

    public SanServiceImpl(SanRepo sanRepo) {
        super(sanRepo);
        this.sanRepo = sanRepo;
    }


    @Override
    public San updateOneDto(Long id, SanCreateFilter filter) {
        San san = getOne(id);

        City city = cityService.getOne(filter.getCityId());

        List<SanType> sanTypes = new ArrayList<>();
        if(filter.getSanTypeId()!= null){
            SanType sanTypeByCode = sanTypeService.getOne(filter.getSanTypeId());
            san.setSanType(sanTypeByCode);
        }

            san.setCity(city);
            san.setName(filter.getName());
            san.setDescription(filter.getDescription());
        if(filter.getSiteLink() != null){
            san.setSiteLink(filter.getSiteLink());
        }
        if(filter.getInstagramLink() != null){
            san.setInstagramLink(filter.getInstagramLink());
        }
        if(filter.getLongitude() != null){
            san.setLongitude(filter.getLongitude());
        }
        if(filter.getLatitude() != null){
            san.setLatitude(filter.getLatitude());
        }

        if(filter.getTelNumbers() != null && filter.getTelNumbers().length > 0){
            List<String> dtoTelNumbers = Arrays.asList(filter.getTelNumbers());
            List<TelNumber> toDelete = new ArrayList<>();
            san.getTelNumbers().stream().forEach(telNumber -> {
                if(!dtoTelNumbers.contains(telNumber.getValue())){
                    toDelete.add(telNumber);
                }
            });

            List<String> sanTelNumberValues = san.getTelNumbers().stream().map(TelNumber::getValue).collect(Collectors.toList());
            List<TelNumber> toCreate = new ArrayList<>();
            dtoTelNumbers.stream().forEach(dtoTelNumber -> {
                if(!sanTelNumberValues.contains(dtoTelNumber)){
                    TelNumber tel = new TelNumber();
                    tel.setValue(dtoTelNumber);
                    telNumberService.addOne(tel);
                    toCreate.add(tel);
                }
            });

            san.deleteTelNumbers(toDelete);
            san.addTelNumbers(toCreate);
        } else{
            san.deleteTelNumbers(san.getTelNumbers());
        }

        return editOneById(san);
    }

    @Override
    public San changeSanType(Long id, Long sanTypeId) {
        San san = getOne(id);

        san.setSanType(sanTypeService.getOne(sanTypeId));

        return san;
    }

    @Override
    public San addTelNumbers(Long id, String[] telNumbers) {
        San san = getOne(id);

        if(telNumbers != null && telNumbers.length > 0){
            List<String> dtoTelNumbers = Arrays.asList(telNumbers);

            List<String> sanTelNumberValues = san.getTelNumbers().stream().map(TelNumber::getValue).collect(Collectors.toList());
            List<TelNumber> toCreate = new ArrayList<>();
            dtoTelNumbers.stream().forEach(dtoTelNumber -> {
                if(!sanTelNumberValues.contains(dtoTelNumber)){
                    TelNumber tel = new TelNumber();
                    tel.setValue(dtoTelNumber);
                    telNumberService.addOne(tel);
                    toCreate.add(tel);
                }
            });
            san.addTelNumbers(toCreate);
        }

        return san;
    }

    @Override
    public void deleteTelNumbers(Long id, String[] telNumbers) {
        San san = getOne(id);

        if(telNumbers != null && telNumbers.length > 0){
            List<String> numbersToDelete = Arrays.asList(telNumbers);

            List<TelNumber> toDelete = new ArrayList<>();
            san.getTelNumbers().stream().forEach(telNumber -> {
                if(numbersToDelete.contains(telNumber.getValue())){
                    toDelete.add(telNumber);
                }
            });
            san.deleteTelNumbers(toDelete);
        }

    }

    @Override
    public Review addReview(Long sanId, ReviewCreateFilter filter) {
        Review review = reviewMapper.reviewCreateFilterToReview(filter);
        San san = getOne(sanId);
        SecUser user = userService.getUserByUsername(filter.getUsername());

        if(filter.getParentReviewId() != null){
            Review parentReview = reviewService.getOne(filter.getParentReviewId());
            review.setParentReview(parentReview);
        }
        review.setSan(san);
        review.setUser(user);

        reviewService.addOne(review);

        return review;
    }

    @Override
    public List<SanForMainDto> getAllForMain(SanForMainFilter filter) {
        List<San> result = sanRepo.getAllBySanForMainFilter(filter.getCityId(), filter.getStartDate(), filter.getEndDate());

        return result.stream().map(san -> {
            SanForMainDto dto = new SanForMainDto();
            dto.setId(san.getId());
            dto.setName(san.getName());
            dto.setDescription(san.getDescription());
            if(san.getMainPicUrl() != null) dto.setPicUrl(APPLICATION_DOWNLOAD_PATH_IMAGE + san.getMainPicUrl());
            dto.setRating(san.getRating());
            dto.setReviewCount(san.getReviewCount());
            dto.setLongitude(san.getLongitude());
            dto.setLatitude(san.getLatitude());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void addGeo(Long sanId, Double longitude, Double latitude) {
        San san = getOne(sanId);
        san.setLatitude(latitude);
        san.setLongitude(longitude);
        editOneById(san);
    }

    @Override
    public List<SysFile> addPics(Long sanId, MultipartFile[] pics) throws IOException {
        San san = getOne(sanId);

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

                san.addPic(file);
            }
        }

        return editOneById(san).getPics();
    }

    @Override
    public void deletePics(Long sanId, Long[] picIds) {
        San san = getOne(sanId);

        List<SysFile> picsToDelete = sysFileService.getAllByIdIn(Arrays.asList(picIds));
        picsToDelete.stream().forEach(file -> {
            file.setDeletedDate(LocalDate.now());
            sysFileService.editOneById(file);
        });
        san.deletePics(picsToDelete);
        editOneById(san);
    }

    @Override
    public List<San> getAllOwn(){
        String currentUsername = authService.getCurrentUsername();
        Organization currentUsersOrganization = organizationService.getOrganizationByTelNumber(currentUsername);
        return repo.getAllByOrganization(currentUsersOrganization);
    }

    @Override
    public boolean checkIfOwnOrg(Long orgId) {
        String currentUsername = authService.getCurrentUsername();
        Organization currentUsersOrganization = organizationService.getOrganizationByTelNumber(currentUsername);
        if(!orgId.equals(currentUsersOrganization.getId())){
            throw new RuntimeException("Данная организация не является вашей!");
        }
        return true;
    }

    @Override
    public boolean checkIfOwnSan(Long sanId) {
        String currentUsername = authService.getCurrentUsername();
        Organization currentUsersOrganization = organizationService.getOrganizationByTelNumber(currentUsername);
        San sanById = getOne(sanId);
        if(!sanById.getOrganization().getId().equals(currentUsersOrganization.getId())){
            throw new RuntimeException("Данный санаторий не является вашей!");
        }
        return true;
    }

    @Override
    protected Class getCurrentClass() {
        return San.class;
    }

    @Override
    public San createSan(SanCreateFilter filter) {
        City city = cityService.getOne(filter.getCityId());

        SanType sanType = sanTypeService.getOne(filter.getSanTypeId());

        Organization organization = organizationService.getOne(filter.getOrgId());
        organization.setConfirmationStatus(OrganizationConfirmationStatus.SERVICE_CREATED);
        organizationService.editOneById(organization);

        San san = new San();
        san.setName(filter.getName());
        san.setDescription(filter.getDescription());
        san.setCity(city);
        san.setOrganization(organization);
        san.setSanType(sanType);

        if(filter.getSiteLink() != null){
            san.setSiteLink(filter.getSiteLink());
        }
        if(filter.getInstagramLink() != null){
            san.setInstagramLink(filter.getInstagramLink());
        }
        if(filter.getLongitude() != null){
            san.setLongitude(filter.getLongitude());
        }
        if(filter.getLatitude() != null){
            san.setLatitude(filter.getLatitude());
        }

        for (String telNumber : filter.getTelNumbers()) {
            TelNumber number = new TelNumber();
            number.setValue(telNumber);
            telNumberService.addOne(number);
            san.addTelNumber(number);
        }

        return addOne(san);
    }

    @Override
    protected Class getServiceClass(){
        return this.getClass();
    }
}
