package kz.open.sankaz.service.impl;

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
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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

    @Value("${payment.ecom.api.base-url}")
    private String ecomBaseUrl;

    @Value("${payment.ecom.merchant.mid}")
    private String ecomMid;

    @Value("${payment.ecom.merchant.tid}")
    private String ecomTid;

    @Value("${payment.ecom.merchant.shared-secred}")
    private String ecomSharedSecred;

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
    public Booking approve(Long bookId, String ecomOrderId) {
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
        booking.setStatus(BookingStatus.APPROVED);
        booking.setApprovedDate(LocalDateTime.now());
        booking.setEcomOrderId(ecomOrderId);
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
            notification.setTitle("Ваша бронь #" + booking.getId() + " подтверждена");
            notification.setTitleKz("Сіздің #" + booking.getId() + " өтініміңіз қабылданды");
            notificationRepo.save(notification);
        }

        return booking;
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
        if(!booking.hasOrderOnEcom()){
            throw new MessageCodeException(PaymentIntegrationCodes.BOOKING_IS_NOT_YET_COMPLETE);
        }

        boolean readyToPay = getEcomOrderPaymentStatus(booking.getEcomOrderId());
        if(!readyToPay){
            throw new MessageCodeException(PaymentIntegrationCodes.BOOKING_IS_NOT_YET_COMPLETE);
        }

        return getBookingPaymentPage(booking);
    }

    private String getBookingPaymentPage(Booking booking) {
//        C_SHARED_KEY
//            .$_POST["ORDER"].";"
//            .$_POST["AMOUNT"].";"
//            .$_POST["CURRENCY"].";"
//            .$_POST["MERCHANT"].";"
//            .$_POST["TERMINAL"].";"
//            .$_POST["NONCE"].";"
//            .$_POST["CLIE NT_ID"].";"
//            .preg_replace("/\n|\r/g","",$_POST["DESC"]).";"
//            .preg_replace("/\n|\r/g","",$_POST["DESC_ORDER"]). ";"
//            .$_POST["EMAIL"].";"
//            .$_POST["BACKREF"].";"
//            .$_POST["Ucaf_Flag"].";"
//            .$_POST["Ucaf_Authentication_Da ta"].";"
//            .$_POST["RECUR_FREQ"].";"
//            .$_POST["RECUR_EXP"].";"
//            .$_POST["INT_REF"].";"
//            .$_POST["RECUR_ REF"].";"
//            .$_POST["PAYMENT_TO"].";"
//            .$_POST["MK_TOKEN"].";"
//            .$_POST["MERCH_TOKEN_ID "].";"
        StringBuilder pSignParams = new StringBuilder(ecomSharedSecred.substring(1));
        pSignParams
                .append(booking.getEcomOrderId()).append(";")
                .append(booking.getSumPrice()).append(";")
                .append("KZT;")
                .append(ecomMid).append(";")
                .append(ecomTid).append(";")
                .append(";")
                .append(";")
                .append("test_desc_").append(booking.getId()).append(";")
                .append(";")
                .append(";")
                .append(";")
                .append(";")
                .append(";")
                .append(";")
                .append(";")
                .append(";")
                .append(";")
                .append(";")
                .append(";")
                .append(";");
        String pSign = DigestUtils.sha512Hex(pSignParams.toString());

        StringBuilder resultUrl = new StringBuilder();
        resultUrl.append(ecomBaseUrl)
                .append("?")
                .append("ORDER=").append(booking.getEcomOrderId())
                .append("&AMOUNT=").append(booking.getSumPrice())
                .append("&CURRENCY=").append("KZT")
                .append("&MERCHANT=").append(ecomMid)
                .append("&TERMINAL=").append(ecomTid)
                .append("&LANGUAGE=ru")
                .append("&DESC=test_desc_").append(booking.getId())
                .append("&P_SIGN=").append(pSign);
        return resultUrl.toString();
    }

    private boolean getEcomOrderPaymentStatus(String orderId) throws IOException {
        StringBuilder resultUrl = new StringBuilder();
        resultUrl.append(ecomBaseUrl)
                .append("?")
                .append("ORDER=").append(orderId)
                .append("&MERCHANT=").append(ecomMid)
                .append("&GETSTATUS=").append(1)
                .append("&LANGUAGE=ru")
                .append("&P_SIGN=").append(DigestUtils.sha512Hex(ecomSharedSecred.substring(1) + orderId + ";" + ecomMid));

        URL url = new URL(resultUrl.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return isStatusSuccess(content.toString());
    }

    private boolean isStatusSuccess(String xmlContent){
        boolean result = true;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            final InputStream stream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
            Document doc = db.parse(stream);
            NodeList results = doc.getElementsByTagName("result");


            if (results.getLength() > 0) {
                Node node = results.item(0);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String code = element.getElementsByTagName("code").item(0).getTextContent();
                    if(code == null || !code.equals("0")){
                        result = false;
                    }

                    NodeList operations = element.getElementsByTagName("operation");
                    if (operations.getLength() > 0) {
                        Node operNode = operations.item(0);
                        if (operNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element operElem = (Element) operNode;
                            String status = operElem.getElementsByTagName("status").item(0).getTextContent();
                            if(status == null || !status.equals("I")){
                                result = false;
                            }
                        }
                    }

                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return result;
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

    @Override
    public void addEcomOrder(Long bookId, String ecomOrderId) {
        Booking booking = getOne(bookId);
        if(booking.hasOrderOnEcom()){
            throw new MessageCodeException(PaymentIntegrationCodes.BOOKING_ALREADY_HAS_AN_ECOM_ORDER);
        }

        booking.setEcomOrderId(ecomOrderId);
        editOneById(booking);

    }
}
