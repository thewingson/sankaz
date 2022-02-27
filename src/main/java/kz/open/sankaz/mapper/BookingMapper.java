package kz.open.sankaz.mapper;

import kz.open.sankaz.model.Booking;
import kz.open.sankaz.pojo.dto.BookingAdminDto;
import kz.open.sankaz.pojo.dto.BookingModerAllDto;
import kz.open.sankaz.pojo.dto.BookingModerByIdDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class BookingMapper {

    @Named("bookingToBookingAdminDto")
    @Mapping(target = "userId", source = "booking.user.id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "telNumber", source = "telNumber")
    @Mapping(target = "adults", source = "adultsCount")
    @Mapping(target = "children", source = "childrenCount")
    @Mapping(target = "roomId", source = "booking.room.id")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "endDate", source = "endDate")
    @Mapping(target = "status", expression = "java( booking.getStatus().name() )")
    @Mapping(target = "approvedDate", source = "approvedDate")
    @Mapping(target = "cancelledDate", source = "cancelledDate")
    @Mapping(target = "paidDate", source = "paidDate")
    abstract public BookingAdminDto bookingToBookingAdminDto(Booking booking);
    @IterableMapping(qualifiedByName = "bookingToBookingAdminDto")
    abstract public List<BookingAdminDto> bookingToBookingAdminDto(List<Booking> bookings);

    @Named("bookingToBookingAllForModerDto")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "roomId", source = "booking.room.id")
    @Mapping(target = "classId", source = "booking.room.roomClassDic.id")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "endDate", source = "endDate")
    @Mapping(target = "status", expression = "java( booking.getStatus().name() )")
    abstract public BookingModerAllDto bookingToBookingAllForModerDto(Booking booking);
    @IterableMapping(qualifiedByName = "bookingToBookingAllForModerDto")
    abstract public List<BookingModerAllDto> bookingToBookingAllForModerDto(List<Booking> bookings);

    @Named("bookingToBookingModerByIdDto")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "telNumber", source = "telNumber")
    @Mapping(target = "adults", source = "adultsCount")
    @Mapping(target = "children", source = "childrenCount")
    @Mapping(target = "className", source = "booking.room.roomClassDic.name")
    @Mapping(target = "roomId", source = "booking.room.id")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "endDate", source = "endDate")
    @Mapping(target = "status", expression = "java( booking.getStatus().name() )")
    @Mapping(target = "price", source = "sumPrice")
    @Mapping(target = "bookedByUser", expression = "java( booking.isBookedByUser() )")
    abstract public BookingModerByIdDto bookingToBookingModerByIdDto(Booking booking);
    @IterableMapping(qualifiedByName = "bookingToBookingModerByIdDto")
    abstract public List<BookingModerByIdDto> bookingToBookingModerByIdDto(List<Booking> bookings);
}
