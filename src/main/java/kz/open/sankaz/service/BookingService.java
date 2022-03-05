package kz.open.sankaz.service;

import kz.open.sankaz.model.Booking;
import kz.open.sankaz.pojo.dto.RoomClassModerCalendarDto;
import kz.open.sankaz.pojo.filter.BookingAdminCreateFilter;
import kz.open.sankaz.pojo.filter.BookingModerCreateFilter;
import kz.open.sankaz.pojo.filter.BookingUserCreateFilter;

import java.util.List;

public interface BookingService extends CommonService<Booking> {
    Booking addOne(BookingAdminCreateFilter filter);
    Booking editOneById(Long bookId, BookingAdminCreateFilter filter);

    List<Booking> getAllBySan(Long sanId);

    List<RoomClassModerCalendarDto> getAllBySan2(Long sanId);

    Booking addOne(BookingModerCreateFilter filter);
    Booking editOneById(Long bookId, BookingModerCreateFilter filter);

    Booking cancel(Long bookId);

    Booking approve(Long bookId);

    Booking pay(Long bookId);

    Booking bookRoomFromUser(Long userId, BookingUserCreateFilter filter);
}
