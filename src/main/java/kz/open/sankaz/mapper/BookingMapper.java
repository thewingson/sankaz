package kz.open.sankaz.mapper;

import kz.open.sankaz.model.Booking;
import kz.open.sankaz.pojo.dto.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class BookingMapper extends AbstractMapper {

    @Autowired
    protected RoomMapper roomMapper;

    @Named("bookingToBookingAdminDto")
    @Mapping(target = "userId", source = "booking.user.id")
    @Mapping(target = "adults", source = "adultsCount")
    @Mapping(target = "children", source = "childrenCount")
    @Mapping(target = "status", expression = "java( booking.getStatus().name() )")
    @Mapping(target = "room", expression = "java( roomMapper.roomToRoomByIdDtoForAdmin( booking.getRoom() ) )")
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

    @Named("bookingToBookingAllForModerDto2")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "roomId", source = "booking.room.id")
    @Mapping(target = "classId", source = "booking.room.roomClassDic.id")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "endDate", source = "endDate")
    @Mapping(target = "status", expression = "java( booking.getStatus().name() )")
    abstract public BookingModerAllDto bookingToBookingAllForModerDto2(Booking booking);
    @IterableMapping(qualifiedByName = "bookingToBookingAllForModerDto2")
    abstract public List<BookingModerAllDto> bookingToBookingAllForModerDto2(List<Booking> bookings);

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

    @Named("bookingToBookingUserDto")
    @Mapping(target = "userId", source = "booking.user.id")
    @Mapping(target = "sanName", source = "booking.room.san.name")
    @Mapping(target = "status", expression = "java( booking.getStatus().name() )")
    abstract public BookingUserDto bookingToBookingUserDto(Booking booking);
    @IterableMapping(qualifiedByName = "bookingToBookingUserDto")
    abstract public List<BookingUserDto> bookingToBookingUserDto(List<Booking> bookings);

    @Named("bookingToBookingUserHistoryDto")
    @Mapping(target = "sanId", source = "booking.room.san.id")
    @Mapping(target = "sanName", source = "booking.room.san.name")
    @Mapping(target = "sanTypeName", source = "booking.room.san.sanType.name")
    @Mapping(target = "sanAddress", source = "booking.room.san.address")
    @Mapping(target = "sanTelNumber", expression = "java( getTelNumberValuesFromEntity(booking.getRoom().getSan().getTelNumbers()) )")
    @Mapping(target = "sanPicUrl", expression = "java( getPicUrlFromSysFile(booking.getRoom().getSan().getMainPic()) )")
    @Mapping(target = "adults", source = "adultsCount")
    @Mapping(target = "children", source = "childrenCount")
    @Mapping(target = "roomCount", source = "booking.room.roomCount")
    @Mapping(target = "bedCount", source = "booking.room.bedCount")
    @Mapping(target = "userId", source = "booking.user.id")
    @Mapping(target = "status", expression = "java( booking.getStatus().name() )")
    abstract public BookingUserHistoryDto bookingToBookingUserHistoryDto(Booking booking);
    @IterableMapping(qualifiedByName = "bookingToBookingUserHistoryDto")
    abstract public List<BookingUserHistoryDto> bookingToBookingUserHistoryDto(List<Booking> bookings);

    @Named("bookingToBookingByIdUserDto")
    @Mapping(target = "userId", source = "booking.user.id")
    @Mapping(target = "orgName", source = "booking.room.san.organization.name")
    @Mapping(target = "sanName", source = "booking.room.san.name")
    @Mapping(target = "sanType", source = "booking.room.san.sanType.name")
    @Mapping(target = "status", expression = "java( booking.getStatus().name() )")
    @Mapping(target = "roomCount", source = "booking.room.roomCount")
    @Mapping(target = "bedCount", source = "booking.room.bedCount")
    abstract public BookingByIdUserDto bookingToBookingByIdUserDto(Booking booking);
    @IterableMapping(qualifiedByName = "bookingToBookingByIdUserDto")
    abstract public List<BookingByIdUserDto> bookingToBookingByIdUserDto(List<Booking> bookings);

    @Named("bookingToBookingModerCalendarDto")
    @Mapping(target = "roomId", source = "booking.room.id")
    @Mapping(target = "userId", source = "booking.user.id")
    @Mapping(target = "adults", source = "adultsCount")
    @Mapping(target = "children", source = "childrenCount")
    @Mapping(target = "status", expression = "java( booking.getStatus().name() )")
    abstract public BookingModerCalendarDto bookingToBookingModerCalendarDto(Booking booking);
    @IterableMapping(qualifiedByName = "bookingToBookingModerCalendarDto")
    abstract public List<BookingModerCalendarDto> bookingToBookingModerCalendarDto(List<Booking> bookings);
}
