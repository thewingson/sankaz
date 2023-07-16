package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.image.SanaTourImage;
import kz.open.sankaz.mapper.ReviewMapper;
import kz.open.sankaz.mapper.SanMapper;
import kz.open.sankaz.model.*;
import kz.open.sankaz.model.enums.OrganizationConfirmationStatus;
import kz.open.sankaz.model.enums.UserNotificationType;
import kz.open.sankaz.model.enums.UserType;
import kz.open.sankaz.pojo.dto.DictionaryLangDto;
import kz.open.sankaz.pojo.dto.SanForMainAdminDto;
import kz.open.sankaz.pojo.dto.SanForMainDto;
import kz.open.sankaz.pojo.filter.*;
import kz.open.sankaz.repo.*;
import kz.open.sankaz.service.*;
import kz.open.sankaz.util.ReSizerImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SanServiceImpl extends AbstractService<San, SanRepo> implements SanService {

    private final SanRepo sanRepo;

    @Autowired
    private StockRepo stockRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserNotificationRepo notificationRepo;

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
SanaTourImageService sanaTourImageService;

@Autowired
    ReSizerImageService reSizerImageService;

    @Autowired
    protected OrganizationService organizationService;



    @Autowired
    private CityService cityService;

    @Autowired
    private SanMapper sanMapper;

    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private SanAdditionalDicService sanAdditionalDicService;
    @Autowired
    @Qualifier("SanAdditionalService")
    private SanAdditionalService sanAdditionalService;




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
        if(filter.getAddress() != null){
            san.setAddress(filter.getAddress());
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
    public Review answerToReview(Long sanId, ReviewModerCreateFilter filter) {
        San san = getOne(sanId);
        Review parentReview = reviewService.getOne(filter.getParentReviewId());

        Review review = new Review();
        review.setUser(san.getOrganization().getUser());
        review.setSan(san);
        review.setText(filter.getText());
        review.setParentReview(parentReview);

        reviewService.addOne(review);

        return review;
    }

    @Override
    public List<SanForMainDto> getAllForMain() {
        List<SanForMainDto> resultList=new ArrayList<>();
    List<San> asd=sanRepo.getAllSanForMain();
    asd.forEach(s->{
        SanForMainDto sanForMainDto = new SanForMainDto();
        sanForMainDto.setName(s.getName());
        String mainImageBase64=sanaTourImageService.getBySanId(s.getId());
        SanType sanType=sanTypeService.getOne(s.getSanType().getId());
        DictionaryLangDto dictionaryLangDto = new DictionaryLangDto();
        dictionaryLangDto.setName(sanType.getName());
        sanForMainDto.setSanType(dictionaryLangDto);

        sanForMainDto.setMainImageBase64(mainImageBase64);
        resultList.add(sanForMainDto);

    });





        return resultList;
    }

    @Override
    public List<SanForMainAdminDto> getAllForMainAdmin(Long userId, SanForMainFilter filter, int page, int size) {
        List<San> result = sanRepo.getAllBySanForMainAdminFilter(
                filter.getCityId(),
                filter.getName().toLowerCase(),
                filter.getSanTypeCode().toLowerCase(),
                page, size);

        List<SanForMainAdminDto> sanForMainDtos = sanMapper.sanToSanForMainAdminDto(result);
        List<Long> favSanIds = repo.getFavSanId(userId);
        sanForMainDtos.forEach(dto -> {
            if(favSanIds.contains(dto.getId())){
                dto.setFav(true);
            }

        });
        return sanForMainDtos;
    }

    @Override
    public List<SanForMainDto> getFavs(Long userId, int page, int size) {
//        List<San> result = sanRepo.getFavs(userId, page, size);
//
//      / /List<SanForMainDto> sanForMainDtos = sanMapper.sanToSanForMainDto(result);
//        List<Long> favSanIds = repo.getFavSanId(userId);
//        sanForMainDtos.forEach(dto -> {
//            //TODO
//           // dto.setFav(true);
//        });
//        return sanForMainDtos;
        return null;
    }

    @Override
    public void addGeo(Long sanId, Double longitude, Double latitude) {
        San san = getOne(sanId);
        san.setLatitude(latitude);
        san.setLongitude(longitude);
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
    public Stock addStock(Long sanId, StockCreateFilter filter) {
        San san = getOne(sanId);
        Stock stock = new Stock();
        stock.setSan(san);
        stock.setTitle(filter.getTitle());
        stock.setTitleKz(filter.getTitleKz());
        stock.setDescription(filter.getDescription());
        stock.setDescriptionKz(filter.getDescriptionKz());
        stockRepo.save(stock);

        Optional<SecRole> admin = roleRepo.findByName("ROLE_ADMIN");
        List<SecUser> activeUsers = userRepo.findAllByUserTypeAndActive(UserType.USER.name(), true);
        List<UserNotification> notifications = new ArrayList<>();
        activeUsers.forEach(user -> {
            if(!user.getRoles().contains(admin.get())){
                UserNotification notification = new UserNotification();
                notification.setUser(user);
                notification.setStock(stock);
                notification.setNotifyDate(LocalDateTime.now());
                notification.setNotificationType(UserNotificationType.STOCK);
                notification.setTitle(stock.getTitle());
                notification.setTitleKz(stock.getTitleKz());
                notification.setDescription(stock.getDescription());
                notification.setDescriptionKz(stock.getDescriptionKz());
                notifications.add(notification);
            }
        });
        notificationRepo.saveAll(notifications);
        //TODO: send to firebase

        return stock;
    }

    @Override
    public Stock editStock(Long stockId, StockCreateFilter filter) {
        Optional<Stock> stock = stockRepo.findById(stockId);
        if (stock.isPresent()) {
            Stock stock1 = stock.get();
            stock1.setTitle(filter.getTitle());
            stock1.setTitleKz(filter.getTitleKz());
            stock1.setDescription(filter.getDescription());
            stock1.setDescriptionKz(filter.getDescriptionKz());
            return stockRepo.save(stock1);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("ID", stockId);
            throw new EntityNotFoundException(Stock.class, params);
        }
    }

    @Override
    public void deleteStock(Long stockId) {
        Optional<Stock> stock = stockRepo.findById(stockId);
        if (stock.isPresent()) {
            Stock stock1 = stock.get();
            stock1.setActive(false);
            stockRepo.save(stock1);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("ID", stockId);
            throw new EntityNotFoundException(Stock.class, params);
        }
    }

    @Override
    protected Class getCurrentClass() {
        return San.class;
    }

    @Override
    public San createSan(SanCreateFilter filter) {
        City city = cityService.getOne(filter.getCityId());
        List<SanaTourImage> sanaTourImages= new ArrayList<>();
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
        if(filter.getAddress() != null){
            san.setAddress(filter.getAddress());
        }

        for (String telNumber : filter.getTelNumbers()) {
            TelNumber number = new TelNumber();
            number.setValue(telNumber);
            telNumberService.addOne(number);
            san.addTelNumber(number);
        }

        San result= addOne(san);
        //Сохранить фото в базу
        for (byte[] imageByte :filter.getImages()){
            SanaTourImage sanaTourImage = new SanaTourImage();
            sanaTourImage.setType("S");
            sanaTourImage.setBase64Original(Base64.getEncoder().encodeToString(imageByte));

            sanaTourImage.setBase64Scaled(reSizerImageService.reSize(imageByte,240,240));
            sanaTourImage.setSanId(result);
            sanaTourImages.add(sanaTourImage);
        }
        sanaTourImageService.saveAll(sanaTourImages);
        //Сохранить Дополнительный услуги в базу
        if (!filter.getSanAdditionalDics().isEmpty()){
            List<SanAdditionalDic> sanAdditionDics=sanAdditionalDicService.findAllByIds(filter.getSanAdditionalDics());

            sanAdditionDics.stream().forEach(e->{
                e.setSanAdditionalList(null);
                SanAdditional sanAdditional = new SanAdditional();
                sanAdditional.setEnable(true);
                sanAdditional.setAdditionalDic(e);
                sanAdditional.setCost(0L);
                sanAdditional.setSan(result);
                sanAdditionalService.save(sanAdditional);


            });

        }
        return result;
    }

    @Override
    public void addFav(Long userId, Long sanId) {
        San san = getOne(sanId);
        SecUser user = userService.getOne(userId);

        List<Long> sanIds = user.getFavorites().stream().map(San::getId).collect(Collectors.toList());
        if(sanIds.contains(sanId)){
            throw new RuntimeException("Вы уже добавили данную запись в избранное");
        }

        user.addFav(san);
        userService.editOneById(user);
    }

    @Override
    public void deleteFav(Long userId, Long sanId) {
        San san = getOne(sanId);
        SecUser user = userService.getOne(userId);

        List<Long> sanIds = user.getFavorites().stream().map(San::getId).collect(Collectors.toList());
        if(!sanIds.contains(sanId)){
            throw new RuntimeException("Вы уже убрали данную запись из избранного");
        }

        user.deleteFav(san);
        userService.editOneById(user);
    }

    @Override
    protected Class getServiceClass(){
        return this.getClass();
    }
}
