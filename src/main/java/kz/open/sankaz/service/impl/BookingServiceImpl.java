package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.BookingCodes;
import kz.open.sankaz.exception.MessageCodeException;
import kz.open.sankaz.model.Booking;
import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.model.enums.BookingStatus;
import kz.open.sankaz.model.enums.UserType;
import kz.open.sankaz.pojo.dto.DatesDto;
import kz.open.sankaz.pojo.dto.RoomClassModerCalendarDto;
import kz.open.sankaz.pojo.filter.BookingAdminCreateFilter;
import kz.open.sankaz.pojo.filter.BookingModerCreateFilter;
import kz.open.sankaz.pojo.filter.BookingUserCreateFilter;
import kz.open.sankaz.repo.BookingRepo;
import kz.open.sankaz.repo.RoomRepo;
import kz.open.sankaz.service.BookingService;
import kz.open.sankaz.service.RoomService;
import kz.open.sankaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<RoomClassModerCalendarDto> getAllBySan2(Long sanId) {

        return null;
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

        return addOne(booking);
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

        return editOneById(booking);
    }

    @Override
    public Booking cancel(Long bookId) {
        Booking booking = getOne(bookId);
        if(booking.isCancelled()){
            throw new MessageCodeException(BookingCodes.BOOKING_IS_ALREADY_CANCELLED);
        }
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledDate(LocalDateTime.now());
        return editOneById(booking);
    }

    @Override
    public Booking approve(Long bookId) {
        Booking booking = getOne(bookId);
        if(!booking.isWaiting()){
            throw new MessageCodeException(BookingCodes.YOU_CAN_APPROVE_ONLY_WITH_WAITING_STATUS);
        }
        List<Booking> activeBookingsInDateRange = repo.getAllByRoomIdAndDateRanges(booking.getRoom().getId(), booking.getStartDate(), booking.getEndDate());
        if(!activeBookingsInDateRange.isEmpty()){
            throw new MessageCodeException(BookingCodes.ROOM_IS_NOT_AVAILABLE);
        }
        booking.setStatus(BookingStatus.APPROVED);
        booking.setCancelledDate(LocalDateTime.now());
        return editOneById(booking);
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
        return editOneById(booking);
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

        return addOne(booking);
    }
}
