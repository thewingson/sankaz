package kz.open.sankaz.mapper;

import kz.open.sankaz.image.SanaTourImage;
import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.RoomClassDic;
import kz.open.sankaz.pojo.dto.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class RoomMapper extends AbstractMapper {

    @Autowired
    protected SanMapper sanMapper;

    @Named("roomToRoomCreateDto")
    @Mapping(target = "roomClassDicId", source = "room.roomClassDic.id")
    @Mapping(target = "sanId", source = "room.san.id")

    abstract public RoomCreateDto roomToRoomCreateDto(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomCreateDto")
    abstract public List<RoomCreateDto> roomToRoomCreateDto(List<Room> rooms);

    @Named("roomToRoomForBookCreateDto")
    @Mapping(target = "roomClassDicId", source = "room.roomClassDic.id")
    @Mapping(target = "roomClassName", source = "room.roomClassDic.currentLocaleName")
    abstract public RoomForBookCreateDto roomToRoomForBookCreateDto(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomForBookCreateDto")
    abstract public List<RoomForBookCreateDto> roomToRoomForBookCreateDto(List<Room> rooms);

    @Named("roomToRoomInSanByIdDto")
    @Mapping(target = "roomClassDicId", source = "room.roomClassDic.id")
    //TODO @Mapping(target = "sanaTourImage", expression = "java( getFirstPicUrlFromSysFiles(room.getPics()) )")
    @Mapping(target = "name", source = "room.roomClassDic.name")
    abstract public RoomInSanByIdDto roomToRoomInSanByIdDto(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomInSanByIdDto")
    abstract public List<RoomInSanByIdDto> roomToRoomInSanByIdDto(List<Room> rooms);

    @Named("roomToRoomByIdDtoForUser")
    @Mapping(target = "classId", source = "room.roomClassDic.id")
    //@Mapping(target = "picUrls", expression = "java( getPicUrlsFromSysFiles(room.getPics()) )")
    @Mapping(target = "name", source = "room.roomClassDic.name")
    @Mapping(target = "description", source = "room.roomClassDic.description")
    abstract public RoomByIdDto roomToRoomByIdDtoForUser(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomByIdDtoForUser")
    abstract public List<RoomByIdDto> roomToRoomByIdDtoForUser(List<Room> rooms);

    @Named("roomToRoomByIdDtoForAdmin")
    @Mapping(target = "san", expression = "java( sanMapper.sanToSanSimpleDto(room.getSan()) )")
   // @Mapping(target = "picUrls", expression = "java( getPicUrlsFromSysFiles(room.getPics()) )")
    @Mapping(target = "name", source = "room.roomClassDic.name")
    @Mapping(target = "description", source = "room.roomClassDic.description")
    abstract public RoomByIdDto roomToRoomByIdDtoForAdmin(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomByIdDtoForAdmin")
    abstract public List<RoomByIdDto> roomToRoomByIdDtoForAdmin(List<Room> rooms);

    protected String getFirstPicUrlFromSysFiles(List<SanaTourImage> pics){
//        if(pics != null && !pics.isEmpty()){
//            return getPicUrlFromSysFile(pics.get(0));
//        }
        return null;
    }

    @Named("roomToRoomInClassDicDto")
    @Mapping(target = "roomNumber", source = "room.roomNumber")
   // @Mapping(target = "mainPic", expression = "java( fileToDto(room.getMainPic()) )")
  //  @Mapping(target = "pics", expression = "java( fileToDto(room.getPics()) )")
    @Mapping(target = "price", source = "room.price")
    @Mapping(target = "priceChild", source = "room.priceChild")
    abstract public RoomInClassDicDto roomToRoomInClassDicDto(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomInClassDicDto")
    abstract public List<RoomInClassDicDto> roomToRoomInClassDicDto(List<Room> rooms);

    @Named("roomClassDicToDto")
    @Mapping(target = "rooms", expression = "java( roomToRoomInClassDicDto( roomClassDic.getRooms() ) )")
    abstract public RoomClassDicDto roomClassDicToDto(RoomClassDic roomClassDic);
    @IterableMapping(qualifiedByName = "roomClassDicToDto")
    abstract public List<RoomClassDicDto> roomClassDicToDto(List<RoomClassDic> roomClassDics);

    @Named("roomToRoomModerCalendarDto")
    @Mapping(target = "bookings", ignore = true)
    abstract public RoomModerCalendarDto roomToRoomModerCalendarDto(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomModerCalendarDto")
    abstract public List<RoomModerCalendarDto> roomToRoomModerCalendarDto(List<Room> rooms);

    @Named("roomClassToRoomClassModerCalendarDto")
    @Mapping(target = "className", source = "roomClassDic.name")
    @Mapping(target = "rooms", expression = "java( roomToRoomModerCalendarDto(roomClassDic.getRooms()) )")
    abstract public RoomClassModerCalendarDto roomClassToRoomClassModerCalendarDto(RoomClassDic roomClassDic);
    @IterableMapping(qualifiedByName = "roomClassToRoomClassModerCalendarDto")
    abstract public List<RoomClassModerCalendarDto> roomClassToRoomClassModerCalendarDto(List<RoomClassDic> roomClassDics);

    @Named("roomClassDicSimpleToDto")
    abstract public RoomClassDicSimpleDto roomClassDicSimpleToDto(RoomClassDic roomClassDic);
    @IterableMapping(qualifiedByName = "roomClassDicSimpleToDto")
    abstract public List<RoomClassDicSimpleDto> roomClassDicSimpleToDto(List<RoomClassDic> roomClassDics);

    @Named("roomToRoomDto")
    @Mapping(target = "san", ignore = true)
  //  @Mapping(target = "picUrls", ignore = true)
    @Mapping(target = "classId", ignore = true)
    @Mapping(target = "name", source = "room.roomClassDic.name")
    @Mapping(target = "description", source = "room.roomClassDic.description")
    abstract public RoomByIdDto roomToRoomDto(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomDto")
    abstract public List<RoomByIdDto> roomToRoomDto(List<Room> rooms);

}
