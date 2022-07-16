package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.exception.MessageCodeException;
import kz.open.sankaz.exception.UserCodes;
import kz.open.sankaz.mapper.BookingMapper;
import kz.open.sankaz.mapper.NotificationMapper;
import kz.open.sankaz.mapper.SecUserMapper;
import kz.open.sankaz.model.*;
import kz.open.sankaz.model.enums.ConfirmationStatus;
import kz.open.sankaz.model.enums.OrganizationConfirmationStatus;
import kz.open.sankaz.model.enums.UserNotificationType;
import kz.open.sankaz.model.enums.UserType;
import kz.open.sankaz.pojo.dto.BookingByIdUserDto;
import kz.open.sankaz.pojo.dto.PageDto;
import kz.open.sankaz.pojo.dto.PictureDto;
import kz.open.sankaz.pojo.dto.TokenDto;
import kz.open.sankaz.pojo.dto.notifications.*;
import kz.open.sankaz.pojo.filter.SecUserEditFilter;
import kz.open.sankaz.pojo.filter.UserCreateFilter;
import kz.open.sankaz.pojo.filter.UserEditFilter;
import kz.open.sankaz.repo.SecUserTokenRepo;
import kz.open.sankaz.repo.StockRepo;
import kz.open.sankaz.repo.UserNotificationRepo;
import kz.open.sankaz.repo.UserRepo;
import kz.open.sankaz.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class UserServiceImpl extends AbstractService<SecUser, UserRepo> implements UserService {

    @Autowired
    private SysFileService fileService;

    @Autowired
    private GenderService genderService;

    @Autowired
    private CityService cityService;

    @Autowired
    private SysFileService sysFileService;

    @Lazy
    @Autowired
    private AuthService authService;

    @Autowired
    private SecUserMapper userMapper;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Value("${application.file.upload.path.image}")
    private String APPLICATION_UPLOAD_PATH_IMAGE;

    @Value("${application.file.download.path.image}")
    private String APPLICATION_DOWNLOAD_PATH_IMAGE;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    private OrganizationService organizationService;

    @Lazy
    @Autowired
    private BookingService bookingService;

    @Autowired
    private SecUserTokenRepo tokenRepo;

    @Autowired
    private UserNotificationRepo notificationRepo;

    @Autowired
    private StockRepo stockRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        super(userRepo);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.getByUsername(username);
    }

    @Override
    public void createUser(UserDetails user) {
        addOne((SecUser) user);
    }

    @Override
    public void updateUser(UserDetails user) {
        editOneById((SecUser) user);
    }


    @Override
    public void deleteUser(String username) {
        deleteOneByUsernameSoft(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new RuntimeException("Method is not supported!");
    }

    @Override
    public boolean userExists(String username) {
        return loadUserByUsername(username) != null;
    }

    @Override
    public List<SecUser> getAll() {
        return repo.getAllByActive(true);
    }

    @Override
    public void deleteOneByUsernameSoft(String username) {
        SecUser user = (SecUser)loadUserByUsername(username);
        repo.save(user);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public SecUser getUserByTelNumber(String telNumber) throws EntityNotFoundException {
        Optional<SecUser> secUser = repo.findByTelNumber(telNumber);
        if(!secUser.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("TEL_NUMBER", telNumber);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return secUser.get();
    }

    @Override
    public SecUser updateProfile(Long id, SecUserEditFilter filter) {
        SecUser user = getOne(id);
        user.setFirstName(filter.getFirstName());
        user.setLastName(filter.getLastName());
        if (filter.getGenderId() != null) {
            Gender gender = genderService.getOne(filter.getGenderId());
            if(!gender.equals(user.getGender())){
                user.setGender(gender);
            }
        }
        if(filter.getCityId() != null){
            City city = cityService.getOne(filter.getCityId());
            if(!city.equals(user.getCity())){
                user.setCity(city);
            }
        }
        return user;
    }

    @Override
    public SecUser createOne(UserCreateFilter filter) {
        SecUser userByNumber = new SecUser();
        try {
            getUserByTelNumber(filter.getTelNumber());
            throw new MessageCodeException(UserCodes.TEL_NUMBER_IS_ALREADY_REGISTERED);
        } catch (EntityNotFoundException e) {
            log.info("Tel. number did not registered {}", filter.getTelNumber());
        }

        if(filter.getCityId() != null){
            City city = cityService.getOne(filter.getCityId());
            userByNumber.setCity(city);
        }
        if(filter.getGenderId() != null){
            Gender gender = genderService.getOne(filter.getGenderId());
            userByNumber.setGender(gender);
        }
        if(!filter.getEmail().isEmpty()){
            try{
                getUserByEmail(filter.getEmail());
                throw new MessageCodeException(UserCodes.EMAIL_IS_ALREADY_REGISTERED);
            } catch (EntityNotFoundException e){
                userByNumber.setEmail(filter.getEmail());
            }
        }
        userByNumber.setPassword(passwordEncoder.encode("Welcome123"));
        userByNumber.setConfirmationStatus(ConfirmationStatus.valueOf(filter.getConfirmationStatus()));
        userByNumber.setUserType(UserType.valueOf(filter.getUserType()));
        userByNumber.setUsername(filter.getUsername());
        userByNumber.setEmail(filter.getEmail());
        userByNumber.setTelNumber(filter.getTelNumber());
        userByNumber.setFirstName(filter.getFirstName());
        userByNumber.setLastName(filter.getLastName());
        userByNumber.setActive(true);
        userByNumber.setConfirmedBy("admin");
        userByNumber.setConfirmedDate(LocalDateTime.now());

        return addOne(userByNumber);
    }

    @Override
    public SecUser editOne(Long userId, UserEditFilter filter) {
        SecUser userByNumber = getOne(userId);
        try {
            SecUser userByTelNumber = getUserByTelNumber(filter.getTelNumber());
            if(!userByNumber.getId().equals(userByTelNumber.getId())){
                throw new MessageCodeException(UserCodes.TEL_NUMBER_IS_ALREADY_REGISTERED);
            }
        } catch (EntityNotFoundException e) {
            userByNumber.setUsername(filter.getTelNumber());
            userByNumber.setTelNumber(filter.getTelNumber());
        }
        if(!filter.getEmail().isEmpty()){
            try{
                SecUser userByEmail = getUserByEmail(filter.getEmail());
                if(!userByNumber.getId().equals(userByEmail.getId())){
                    throw new MessageCodeException(UserCodes.EMAIL_IS_ALREADY_REGISTERED);
                }
            } catch (EntityNotFoundException e){
                userByNumber.setEmail(filter.getEmail());
            }
        }
        if(filter.getCityId() != null){
            City city = cityService.getOne(filter.getCityId());
            userByNumber.setCity(city);
        }
        if(filter.getGenderId() != null){
            Gender gender = genderService.getOne(filter.getGenderId());
            userByNumber.setGender(gender);
        }
        userByNumber.setConfirmationStatus(ConfirmationStatus.valueOf(filter.getConfirmationStatus()));
        userByNumber.setUserType(UserType.valueOf(filter.getUserType()));
        userByNumber.setFirstName(filter.getFirstName());
        userByNumber.setLastName(filter.getLastName());

        return editOneById(userByNumber);
    }

    @Override
    public SysFile addPic(Long userId, MultipartFile pic) throws IOException {
        log.info(getServiceClass().getSimpleName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " Started");
        SecUser user = getOne(userId);

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

            user.setPic(file);
        }

        return editOneById(user).getPic();
    }

    @Override
    public void deletePic(Long userId) {
        log.info(getServiceClass().getSimpleName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " Started");
        SecUser user = getOne(userId);
        SysFile pic = user.getPic();
        pic.setDeletedDate(LocalDate.now());
        sysFileService.editOneById(pic);
        user.setPic(null);
        editOneById(user);
    }

    @Override
    public TokenDto changePassword(Long id, String password, String confirmPassword) {
        if(!password.equals(confirmPassword)){
            throw new RuntimeException("Пароли не совпадают");
        }

        SecUser user = getOne(id);
        user.setPassword(passwordEncoder.encode(password));
        editOneById(user);

        List<SecUserToken> tokens = tokenRepo.findAllByUser(user);
        tokens.forEach(token -> {
            token.setIsBlocked(true);
            token.setBlockDate(LocalDateTime.now());
        });
        tokenRepo.saveAll(tokens);

        return authService.authenticateUser(user.getUsername(), password);
    }

    @Override
    public PictureDto changePicture(Long userId, MultipartFile file) throws IOException {
        SecUser user = getOne(userId);
        String fileNameWithPath = "";
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new RuntimeException("Wrong file!");
        }

        File uploadDir = new File(APPLICATION_UPLOAD_PATH_IMAGE);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();
        fileNameWithPath = APPLICATION_UPLOAD_PATH_IMAGE + "/" + resultFilename;

        file.transferTo(new File(fileNameWithPath));

        SysFile sysFile = new SysFile();
        sysFile.setExtension(file.getContentType());
        sysFile.setFileName(file.getOriginalFilename());
        sysFile.setSize(file.getSize());
        fileService.addOne(sysFile);

        user.setPic(sysFile);
        editOneById(user);
        return new PictureDto(APPLICATION_DOWNLOAD_PATH_IMAGE + "/" + resultFilename);
    }

    @Override
    public void deletePicture(Long userId) {
        // TODO: remove pic file
        SecUser user = getOne(userId);
        user.setPic(null);
        editOneById(user);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public SecUser getUserByEmail(String email) {
        Optional<SecUser> secUser = repo.findByEmail(email);
        if(!secUser.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("email", email);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return secUser.get();
    }

    @Override
    public SecUser getUserByUsername(String username) {
        Optional<SecUser> secUser = repo.findByUsername(username);
        if(!secUser.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("username", username);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return secUser.get();
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = SQLException.class)
    @Override
    public void deleteOneById(Long id) {
        SecUser user = getOne(id);
        List<Organization> organizationList = new ArrayList<>();
        user.getOrganizations().forEach(organization -> {
            organizationList.add(organization);
            organization.setUser(null);
            organization.setConfirmationStatus(OrganizationConfirmationStatus.REJECTED);
            organization.setRejectMessage("User is deleted");
            organization.setEmail("deleted_" + organization.getEmail());
            organization.setIban("deleted_" + organization.getIban());
            organization.setIin("deleted_" + organization.getIin());
            organization.setTelNumber("deleted_" + organization.getTelNumber());
        });
        organizationService.saveAll(organizationList);

        List<Booking> bookings = new ArrayList<>();
        bookingService.getAllByUser(user).forEach(booking -> {
            bookings.add(booking);
            booking.setUser(null);
        });
        bookingService.saveAll(bookings);

        user.setUsername("deleted_" + user.getUsername());
        user.setTelNumber("deleted_" + user.getTelNumber());
        user.setEmail("deleted_" + user.getEmail());
        user.setActive(false);
        repo.save(user);
    }

    @Override
    public Page<SecUser> getAll(int page, int size) {
        return repo.getAllByActive(true, PageRequest.of(page, size));
    }

    @Override
    public PageDto getAllPage(int page, int size) {
        Page<SecUser> pages = getAll(page, size);
        PageDto dto = new PageDto();
        dto.setTotal(pages.getTotalElements());
        dto.setContent(userMapper.userToDto(pages.getContent()));
        dto.setPageable(pages.getPageable());
        return dto;
    }

    @Override
    public List<SecUser> getAllPageWithFilter(String fullName, String telNumber, int page, int size) {
        return repo.getAllForNewOrganization(true, fullName, telNumber, page, size);
    }

    @Override
    public UserNotificationDto getNotifications(Long userId, int page, int size) {
        List<UserNotification> notifications = notificationRepo.findAllByUserId(userId, PageRequest.of(page, size, Sort.by("notifyDate").descending()));
        int notViewedCount = notificationRepo.getNotViewedNotificationsCount(userId, false);
        UserNotificationDto dto = notificationsToDto(notifications);
        dto.setNewNotifyCount(notViewedCount);
        return dto;
    }

    @Override
    public void viewNotification(Long notId) {
        Optional<UserNotification> notification = notificationRepo.findById(notId);
        if(notification.isPresent()){
            notification.get().setViewed(true);
            notificationRepo.save(notification.get());

            Stock stock = notification.get().getStock();
            if(stock != null){
                stock.setViewCount(stock.getViewCount() + 1);
                stockRepo.save(stock);
            }
        }
    }

    private UserNotificationDto notificationsToDto(List<UserNotification> notifications){
        UserNotificationDto dto = new UserNotificationDto();

        List<UserNotification> stockNots = notifications
                .stream()
                .filter(userNotification -> userNotification.getNotificationType().equals(UserNotificationType.STOCK))
                .collect(Collectors.toList());
        StockNotificationDto stockNotificationDto = new StockNotificationDto();
        stockNotificationDto.setNewNotifyCount((int) stockNots
                .stream()
                .filter(userNotification -> !userNotification.isViewed())
                .count());
        stockNotificationDto.setStocks(stockNots
                .stream()
                .map(this::stockToDto)
                .collect(Collectors.toList()));
        dto.setStockNotification(stockNotificationDto);

        List<UserNotification> bookNots = notifications
                .stream()
                .filter(userNotification -> userNotification.getNotificationType().equals(UserNotificationType.BOOKING))
                .collect(Collectors.toList());
        dto.setBookingNotification(bookToDto(bookNots));

        List<UserNotification> paymentNots = notifications
                .stream()
                .filter(userNotification -> userNotification.getNotificationType().equals(UserNotificationType.PAYMENT))
                .collect(Collectors.toList());
        dto.setPaymentNotification(paymentToDto(paymentNots));

        int payments = dto.getPaymentNotification().getNewNotifyCount() == 0 ? 0 : 1;
        int books = dto.getBookingNotification().getNewNotifyCount() == 0 ? 0 : 1;
        int stocks = dto.getStockNotification().getNewNotifyCount() == 0 ? 0 : 1;
        int newNotsCount = payments + books + stocks;
        dto.setNewNotifyCount(newNotsCount);

        return dto;
    }

    private StockNotItemDto stockToDto(UserNotification notification){
        StockNotItemDto dto = new StockNotItemDto();
        dto.setId(notification.getId());
        dto.setNotifyDate(notification.getNotifyDate());
        dto.setViewed(notification.isViewed());

        StockDto stockDto = notificationMapper.stockToDto(notification.getStock());
        dto.setStock(stockDto);
        return dto;
    }

    private BookingNotificationDto bookToDto(List<UserNotification> notifications){
        BookingNotificationDto dto = new BookingNotificationDto();
        AtomicInteger newNotsCount = new AtomicInteger();

        List<BookNotItemDto> bookNotItemDtos = new ArrayList<>();
        notifications.forEach(userNotification -> {
            BookNotItemDto bookNotItemDto = new BookNotItemDto();
            bookNotItemDto.setId(userNotification.getId());
            bookNotItemDto.setViewed(userNotification.isViewed());
            bookNotItemDto.setNotifyDate(userNotification.getNotifyDate());
            bookNotItemDto.setTitle(userNotification.getTitle());
            bookNotItemDto.setTitleKz(userNotification.getTitleKz());

            BookingByIdUserDto booking = bookingMapper.bookingToBookingByIdUserDto(userNotification.getBooking());
            booking.setStatus(userNotification.getBooking().getStatus().name());
            bookNotItemDto.setBooking(booking);
            bookNotItemDtos.add(bookNotItemDto);
            if(!userNotification.isViewed()) newNotsCount.getAndIncrement();
        });
        dto.setBooks(bookNotItemDtos);
        dto.setNewNotifyCount(newNotsCount.get());
        return dto;
    }

    private PaymentNotificationDto paymentToDto(List<UserNotification> notifications){
        PaymentNotificationDto dto = new PaymentNotificationDto();
        AtomicInteger newNotsCount = new AtomicInteger();

        List<BookNotItemDto> bookNotItemDtos = new ArrayList<>();
        notifications.forEach(userNotification -> {
            BookNotItemDto bookNotItemDto = new BookNotItemDto();
            bookNotItemDto.setId(userNotification.getId());
            bookNotItemDto.setViewed(userNotification.isViewed());
            bookNotItemDto.setNotifyDate(userNotification.getNotifyDate());
            bookNotItemDto.setTitle(userNotification.getTitle());
            bookNotItemDto.setTitleKz(userNotification.getTitleKz());

            BookingByIdUserDto booking = bookingMapper.bookingToBookingByIdUserDto(userNotification.getBooking());
            booking.setStatus(userNotification.getBooking().getStatus().name());
            bookNotItemDto.setBooking(booking);
            bookNotItemDtos.add(bookNotItemDto);
            if(!userNotification.isViewed()) newNotsCount.getAndIncrement();
        });
        dto.setBooks(bookNotItemDtos);
        dto.setNewNotifyCount(newNotsCount.get());
        return dto;
    }

    @Override
    protected Class getCurrentClass() {
        return SecUser.class;
    }

}
