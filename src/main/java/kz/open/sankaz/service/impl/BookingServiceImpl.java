package kz.open.sankaz.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.open.sankaz.exception.BookingCodes;
import kz.open.sankaz.exception.MessageCodeException;
import kz.open.sankaz.exception.PaymentIntegrationCodes;
import kz.open.sankaz.mapper.BookingMapper;
import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.model.*;
import kz.open.sankaz.model.enums.BookingStatus;
import kz.open.sankaz.model.enums.UserNotificationType;
import kz.open.sankaz.model.enums.UserType;
import kz.open.sankaz.pojo.dto.BookingModerCalendarDto;
import kz.open.sankaz.pojo.dto.DatesDto;
import kz.open.sankaz.pojo.dto.RoomClassModerCalendarDto;
import kz.open.sankaz.pojo.dto.RoomModerCalendarDto;
import kz.open.sankaz.pojo.dto.payment.InvoiceCreateDto;
import kz.open.sankaz.pojo.dto.payment.InvoiceCreateResponseDto;
import kz.open.sankaz.pojo.dto.payment.UserAuthDto;
import kz.open.sankaz.pojo.dto.payment.UserLoginDto;
import kz.open.sankaz.pojo.filter.BookingAdminCreateFilter;
import kz.open.sankaz.pojo.filter.BookingModerCreateFilter;
import kz.open.sankaz.pojo.filter.BookingUserCreateFilter;
import kz.open.sankaz.repo.BookingHistoryRepo;
import kz.open.sankaz.repo.BookingRepo;
import kz.open.sankaz.repo.RoomRepo;
import kz.open.sankaz.repo.UserNotificationRepo;
import kz.open.sankaz.repo.dictionary.RoomClassDicRepo;
import kz.open.sankaz.service.BookingService;
import kz.open.sankaz.service.RoomService;
import kz.open.sankaz.service.UserService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BookingServiceImpl extends AbstractService<Booking, BookingRepo> implements BookingService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private UserNotificationRepo notificationRepo;

    @Autowired
    private RoomClassDicRepo roomClassDicRepo;

    @Autowired
    private BookingHistoryRepo bookingHistoryRepo;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private BookingMapper bookingMapper;

    @Value("${payment.api.base-url}")
    private String paymentBaseUrl;

    @Value("${payment.model.prefix}")
    private String paymentModelPrefix;

    @Value("${payment.operation.create}")
    private String paymentOperationCreate;

    @Value("${payment.operation.auth}")
    private String paymentOperationAuth;

    @Value("${payment.merchant.login}")
    private String paymentMerchantLogin;

    @Value("${payment.merchant.password}")
    private String paymentMerchantPassword;

    @Value("${payment.test-user.card-number}")
    private String paymentTestUserCardNumber;

    @Value("${payment.test-user.card-exp}")
    private String paymentTestUserCardExp;

    @Value("${payment.test-user.card-cvv}")
    private String paymentTestUserCardCvv;

    public BookingServiceImpl(BookingRepo bookingRepo) {
        super(bookingRepo);
    }

    @Override
    protected Class getCurrentClass() {
        return Booking.class;
    }

    @Override
    protected Class getServiceClass(){
        return this.getClass();
    }

    @Override
    public Booking addOne(BookingAdminCreateFilter filter) {
        Booking booking = new Booking();
        if(filter.getUserId() != null){
            SecUser user = userService.getOne(filter.getUserId());
            if(user.getUserType().equals(UserType.ORG)){
                throw new MessageCodeException(BookingCodes.USER_TYPE_CAN_NOT_BE_ORG);
            }
            booking.setUser(user);
            booking.setFirstName(user.getFirstName());
            booking.setLastName(user.getLastName());
            booking.setTelNumber(user.getTelNumber());
        } else {
            booking.setFirstName(filter.getFirstName());
            booking.setLastName(filter.getLastName());
            booking.setTelNumber(filter.getTelNumber());
        }
        Room room = roomService.getOne(filter.getRoomId());
        booking.setRoom(room);
        booking.setAdultsCount(filter.getAdults());
        booking.setChildrenCount(filter.getChildren());
        booking.setStartDate(filter.getStartDate());
        booking.setEndDate(filter.getEndDate());

        booking.setStatus(BookingStatus.valueOf(filter.getStatus()));
        booking.setApprovedDate(filter.getApprovedDate());
        booking.setCancelledDate(filter.getCancelledDate());
        booking.setPaidDate(filter.getPaidDate());
        booking.setSumPrice(room.getPrice());

        return addOne(booking);
    }

    @Override
    public Booking editOneById(Long bookId, BookingAdminCreateFilter filter) {
        Booking booking = getOne(bookId);
        if(filter.getUserId() != null){
            SecUser user = userService.getOne(filter.getUserId());
            if(user.getUserType().equals(UserType.ORG)){
                throw new MessageCodeException(BookingCodes.USER_TYPE_CAN_NOT_BE_ORG);
            }
            booking.setUser(user);
            booking.setFirstName(user.getFirstName());
            booking.setLastName(user.getLastName());
            booking.setTelNumber(user.getTelNumber());
        } else {
            booking.setUser(null);
            booking.setFirstName(filter.getFirstName());
            booking.setLastName(filter.getLastName());
            booking.setTelNumber(filter.getTelNumber());
        }
        Room room = roomService.getOne(filter.getRoomId());
        booking.setRoom(room);
        booking.setAdultsCount(filter.getAdults());
        booking.setChildrenCount(filter.getChildren());
        booking.setStartDate(filter.getStartDate());
        booking.setEndDate(filter.getEndDate());

        booking.setStatus(BookingStatus.valueOf(filter.getStatus()));
        booking.setApprovedDate(filter.getApprovedDate());
        booking.setCancelledDate(filter.getCancelledDate());
        booking.setPaidDate(filter.getPaidDate());
        booking.setSumPrice(room.getPrice());

        return editOneById(booking);
    }

    @Override
    public List<Booking> getAllBySan(Long sanId) {
        return repo.getAllBySanId(sanId);
    }

    @Override
    public List<RoomClassModerCalendarDto> getBookingCalendar(Long sanId, LocalDateTime startDate, LocalDateTime endDate) {
        List<RoomClassDic> classes = roomClassDicRepo.getRoomClassDicBySanId(sanId);
        List<Long> roomIds = new ArrayList<>();
        classes.forEach(roomClassDic -> {
            roomClassDic.getRooms().forEach(room -> roomIds.add(room.getId()));
        });
        List<Booking> bookings = repo.getBookingCalendar(roomIds, startDate.toLocalDate(), endDate.toLocalDate());
        List<RoomClassModerCalendarDto> classDtos = roomMapper.roomClassToRoomClassModerCalendarDto(classes);
        List<BookingModerCalendarDto> bookingDtos = bookingMapper.bookingToBookingModerCalendarDto(bookings);

        bookingDtos.forEach(bookingDto -> {
            for (RoomClassModerCalendarDto classDto : classDtos) {
                for (RoomModerCalendarDto roomDto : classDto.getRooms()) {
                    if(roomDto.getId().equals(bookingDto.getRoomId())){
                        roomDto.getBookings().add(bookingDto);
                        break;
                    }
                }
            }
        });
        return classDtos;
    }

    @Override
    public Booking addOne(BookingModerCreateFilter filter) {
        Booking booking = new Booking();
        BookingStatus bookingStatus = BookingStatus.valueOf(filter.getStatus());
        if(bookingStatus.equals(BookingStatus.APPROVED)){
            booking.setApprovedDate(LocalDateTime.now());
        } else if(bookingStatus.equals(BookingStatus.PAID)){
            booking.setPaidDate(LocalDateTime.now());
        } else {
            throw new MessageCodeException(BookingCodes.STATUS_IS_NOT_AVAILABLE_FOR_THIS_OPERATION);
        }
        booking.setStatus(bookingStatus);
        Room room = roomService.getOne(filter.getRoomId());
        booking.setRoom(room);
        booking.setFirstName(filter.getFirstName());
        booking.setLastName(filter.getLastName());
        booking.setTelNumber(filter.getTelNumber());
        booking.setChildrenCount(filter.getChildren());
        booking.setAdultsCount(filter.getAdults());
        booking.setEndDate(filter.getEndDate());
        booking.setStartDate(filter.getStartDate());
        booking.setSumPrice(filter.getPrice());

        addOne(booking);

        BookingHistory history = new BookingHistory();
        history.setStatus(booking.getStatus());
        history.setBooking(booking);
        bookingHistoryRepo.save(history);

        return booking;

    }

    @Override
    public Booking editOneById(Long bookId, BookingModerCreateFilter filter) {
        Booking booking = getOne(bookId);
        BookingStatus filterBookingStatus = BookingStatus.valueOf(filter.getStatus());
        if(!filterBookingStatus.equals(booking.getStatus())){
            if(filterBookingStatus.equals(BookingStatus.APPROVED)){
                List<Booking> activeBookingsInDateRange = repo.getAllByRoomIdAndDateRanges(filter.getRoomId(), booking.getStartDate(), booking.getEndDate());
                if(!activeBookingsInDateRange.isEmpty()){
                    throw new MessageCodeException(BookingCodes.ROOM_IS_NOT_AVAILABLE);
                }
                booking.setStatus(BookingStatus.APPROVED);
                booking.setApprovedDate(LocalDateTime.now());
            } else if(filterBookingStatus.equals(BookingStatus.PAID)){
                if(!booking.isApproved()){
                    List<Booking> activeBookingsInDateRange = repo.getAllByRoomIdAndDateRanges(filter.getRoomId(), booking.getStartDate(), booking.getEndDate());
                    if(!activeBookingsInDateRange.isEmpty()){
                        throw new MessageCodeException(BookingCodes.ROOM_IS_NOT_AVAILABLE);
                    }
                }
                booking.setStatus(BookingStatus.PAID);
                booking.setPaidDate(LocalDateTime.now());
            } else if(filterBookingStatus.equals(BookingStatus.CANCELLED)){
                booking.setStatus(BookingStatus.CANCELLED);
                booking.setCancelledDate(LocalDateTime.now());
            } else {
                throw new MessageCodeException(BookingCodes.STATUS_IS_NOT_AVAILABLE_FOR_THIS_OPERATION);
            }
        }

        Room room = roomService.getOne(filter.getRoomId());
        booking.setRoom(room);
        booking.setFirstName(filter.getFirstName());
        booking.setLastName(filter.getLastName());
        booking.setTelNumber(filter.getTelNumber());
        booking.setChildrenCount(filter.getChildren());
        booking.setAdultsCount(filter.getAdults());
        booking.setEndDate(filter.getEndDate());
        booking.setStartDate(filter.getStartDate());
        booking.setSumPrice(filter.getPrice());

        editOneById(booking);

        BookingHistory history = new BookingHistory();
        history.setStatus(booking.getStatus());
        history.setBooking(booking);
        bookingHistoryRepo.save(history);

        return booking;
    }

    @Override
    public Booking cancel(Long bookId) {
        Booking booking = getOne(bookId);
        if(booking.isCancelled()){
            throw new MessageCodeException(BookingCodes.BOOKING_IS_ALREADY_CANCELLED);
        }
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledDate(LocalDateTime.now());
        editOneById(booking);

        BookingHistory history = new BookingHistory();
        history.setStatus(booking.getStatus());
        history.setBooking(booking);
        bookingHistoryRepo.save(history);

        if(booking.getUser() != null){
            //TODO: send to firebase
            UserNotification notification = new UserNotification();
            notification.setUser(booking.getUser());
            notification.setNotificationType(UserNotificationType.BOOKING);
            notification.setNotifyDate(LocalDateTime.now());
            notification.setBookingHistory(history);
            notification.setTitle("Ваша бронь #" + booking.getId() + " отменена");
            notification.setTitleKz("Сіздің #" + booking.getId() + " өтініміңіз кері қайтарылды");
            notificationRepo.save(notification);
        }

        return booking;
    }

    @Override
    public Booking approve(Long bookId) throws IOException {
        Booking booking = getOne(bookId);
        if(!booking.isWaiting()){
            throw new MessageCodeException(BookingCodes.YOU_CAN_APPROVE_ONLY_WITH_WAITING_STATUS);
        }
        List<LocalDate> busyDates = new ArrayList<>();
        List<DatesDto> availabilityForDateRange = roomRepo.getRoomAvailabilityForDateRange(booking.getRoom().getId(), booking.getStartDate(), booking.getEndDate());
        availabilityForDateRange.forEach(datesDto -> {
            if(!datesDto.isFree()){
                busyDates.add(datesDto.getCheckDate());
            }
        });
        if(!busyDates.isEmpty()){
            Map<String, List<LocalDate>> data = new HashMap<>();
            data.put("busyDates", busyDates);
            throw new MessageCodeException(BookingCodes.ROOM_IS_BUSY_IN_CHOSEN_DATE_RANGE, data);
        }

        UserAuthDto userAuthDto = loginToPaymentService();
        InvoiceCreateResponseDto invoiceCreateResponseDto = createInvoiceInPayment(userAuthDto.getToken().substring(4), booking);

        booking.setStatus(BookingStatus.APPROVED);
        booking.setApprovedDate(LocalDateTime.now());
        booking.setWoopOrderId(paymentModelPrefix + booking.getId());
        booking.setPaymentUrl(invoiceCreateResponseDto.getOperation_url());
        editOneById(booking);

        BookingHistory history = new BookingHistory();
        history.setStatus(booking.getStatus());
        history.setBooking(booking);
        bookingHistoryRepo.save(history);

        if(booking.getUser() != null){
//            TODO: send to firebase
            UserNotification notification = new UserNotification();
            notification.setUser(booking.getUser());
            notification.setNotificationType(UserNotificationType.PAYMENT);
            notification.setNotifyDate(LocalDateTime.now());
            notification.setBookingHistory(history);
            notification.setTitle("Ваша бронь #" + booking.getId() + " подтверждена");
            notification.setTitleKz("Сіздің #" + booking.getId() + " өтініміңіз қабылданды");
            notificationRepo.save(notification);
        }

        return booking;
    }

    private UserAuthDto loginToPaymentService() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserLoginDto userLoginDto = new UserLoginDto(paymentMerchantLogin, paymentMerchantPassword);
        String userLoginJson = objectMapper.writeValueAsString(userLoginDto);
        StringEntity entity = new StringEntity(userLoginJson);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(paymentBaseUrl + paymentOperationAuth);

        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode != 200){
            throw new MessageCodeException(PaymentIntegrationCodes.ERROR_IN_PAYMENT_INTEGRATION);
        }
        HttpEntity responseEntity = response.getEntity();
        String json = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
        client.close();
        return objectMapper.readValue(json, UserAuthDto.class);
    }

    private InvoiceCreateResponseDto createInvoiceInPayment(String authJwt, Booking booking) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InvoiceCreateDto invoiceCreateDto = new InvoiceCreateDto();
        invoiceCreateDto.setReference_id(paymentModelPrefix + booking.getId());
        invoiceCreateDto.setAmount(booking.getSumPrice());
        invoiceCreateDto.setUser_phone(booking.getTelNumber());
        invoiceCreateDto.setEmail("");
        invoiceCreateDto.setMerchant_name(booking.getRoom().getSan().getName());
        invoiceCreateDto.setBack_url("https://www.test.wooppay.com");
        invoiceCreateDto.setDescription("from_backend");
        invoiceCreateDto.setDeath_date(LocalDateTime.now().plusDays(1).toString());
        invoiceCreateDto.setOption("4");
        String userLoginJson = objectMapper.writeValueAsString(invoiceCreateDto);
        StringEntity entity = new StringEntity(userLoginJson);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(paymentBaseUrl + paymentOperationCreate);

        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + authJwt);

        CloseableHttpResponse response = client.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode != 200){
            throw new MessageCodeException(PaymentIntegrationCodes.ERROR_IN_PAYMENT_INTEGRATION);
        }
        HttpEntity responseEntity = response.getEntity();
        String json = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
        client.close();
        return objectMapper.readValue(json, InvoiceCreateResponseDto.class);
    }

    @Override
    public String getPaymentPage(Long bookId) throws IOException {
        Booking booking = getOne(bookId);
        if(booking.isPaid()){
            throw new MessageCodeException(BookingCodes.BOOKING_IS_ALREADY_PAID);
        }
        if(!booking.isApproved()){
            throw new MessageCodeException(BookingCodes.BOOKING_IS_NOT_APPROVED);
        }
        if(!booking.hasOrderOnWoop()){
            throw new MessageCodeException(PaymentIntegrationCodes.BOOKING_IS_NOT_YET_COMPLETE);
        }

        return booking.getPaymentUrl();
    }

    @Override
    public Booking pay(Long bookId) {
        Booking booking = getOne(bookId);
        if(booking.isPaid()){
            throw new MessageCodeException(BookingCodes.BOOKING_IS_ALREADY_PAID);
        }
        if(!booking.isApproved()){
            throw new MessageCodeException(BookingCodes.BOOKING_IS_NOT_APPROVED);
        }

        booking.setStatus(BookingStatus.PAID);
        booking.setPaidDate(LocalDateTime.now());
        editOneById(booking);

        BookingHistory history = new BookingHistory();
        history.setStatus(booking.getStatus());
        history.setBooking(booking);
        bookingHistoryRepo.save(history);

        if(booking.getUser() != null){
            //TODO: send to firebase
            UserNotification notification = new UserNotification();
            notification.setUser(booking.getUser());
            notification.setNotificationType(UserNotificationType.PAYMENT);
            notification.setNotifyDate(LocalDateTime.now());
            notification.setBookingHistory(history);
            notification.setTitle("Ваша бронь #" + booking.getId() + " успешно оплачено");
            notification.setTitleKz("Сіздің #" + booking.getId() + " өтініміңіз төленді");
            notificationRepo.save(notification);
        }
        return booking;
    }

    @Override
    public Booking bookRoomFromUser(Long userId, BookingUserCreateFilter filter) {
        SecUser user = userService.getOne(userId);

        List<LocalDate> busyDates = new ArrayList<>();
        List<DatesDto> availabilityForDateRange = roomRepo.getRoomAvailabilityForDateRange(filter.getRoomId(), filter.getStartDate(), filter.getEndDate());
        availabilityForDateRange.forEach(datesDto -> {
            if(!datesDto.isFree()){
                busyDates.add(datesDto.getCheckDate());
            }
        });
        if(!busyDates.isEmpty()){
            Map<String, List<LocalDate>> data = new HashMap<>();
            data.put("busyDates", busyDates);
            throw new MessageCodeException(BookingCodes.ROOM_IS_BUSY_IN_CHOSEN_DATE_RANGE, data);
        }

        Booking booking = new Booking();
        booking.setUser(userService.getOne(userId));
        booking.setFirstName(user.getFirstName());
        booking.setLastName(user.getLastName());
        booking.setTelNumber(user.getTelNumber());
        booking.setRoom(roomService.getOne(filter.getRoomId()));
        booking.setStartDate(filter.getStartDate());
        booking.setEndDate(filter.getEndDate());
        booking.setStatus(BookingStatus.WAITING);
        booking.setChildrenCount(filter.getChildren());
        booking.setAdultsCount(filter.getAdults());
        booking.setSumPrice(filter.getPrice());

        addOne(booking);

        BookingHistory history = new BookingHistory();
        history.setStatus(booking.getStatus());
        history.setBooking(booking);
        bookingHistoryRepo.save(history);

        // TODO: send notification to firebase
        UserNotification notification = new UserNotification();
        notification.setUser(user);
        notification.setNotificationType(UserNotificationType.BOOKING);
        notification.setBookingHistory(history);
        notification.setNotifyDate(LocalDateTime.now());
        notification.setTitle("Ваша бронь #" + booking.getId() + " рассматривается");
        notification.setTitleKz("Сіздің #" + booking.getId() + " өтініміңіз қарастырылуда");
        notificationRepo.save(notification);

        return booking;
    }

    @Override
    public List<Booking> getAllByUser(SecUser user) {
        return repo.getAllByUserId(user.getId());
    }

}
