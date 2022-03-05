package kz.open.sankaz.mapper;

import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.RoomClass;
import kz.open.sankaz.model.RoomClassDic;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.pojo.dto.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class RoomMapper extends AbstractMapper {

    @Autowired
    protected SanMapper sanMapper;

    @Named("roomToRoomCreateDto")
    @Mapping(target = "roomClassDicId", source = "room.roomClassDic.id")
    abstract public RoomCreateDto roomToRoomCreateDto(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomCreateDto")
    abstract public List<RoomCreateDto> roomToRoomCreateDto(List<Room> rooms);

    private Long roomClassDicId;
    private Long roomClassName;
    @Named("roomToRoomForBookCreateDto")
    @Mapping(target = "roomClassDicId", source = "room.roomClassDic.id")
    @Mapping(target = "roomClassName", source = "room.roomClassDic.currentLocaleName")
    abstract public RoomForBookCreateDto roomToRoomForBookCreateDto(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomForBookCreateDto")
    abstract public List<RoomForBookCreateDto> roomToRoomForBookCreateDto(List<Room> rooms);

//    @Named("roomToRoomInSanByIdDto")
//    @Mapping(target = "mainPicUrl", expression = "java( getFirstPicUrlFromSysFiles(roomClass.getPics()) )")
//    abstract public RoomInSanByIdDto roomToRoomInSanByIdDto(RoomClass roomClass);
//    @IterableMapping(qualifiedByName = "roomToRoomInSanByIdDto")
//    abstract public List<RoomInSanByIdDto> roomToRoomInSanByIdDto(List<RoomClass> roomClasses);
    @Named("roomToRoomInSanByIdDto")
    @Mapping(target = "mainPicUrl", expression = "java( getFirstPicUrlFromSysFiles(room.getPics()) )")
    @Mapping(target = "name", source = "room.roomClassDic.name")
    abstract public RoomInSanByIdDto roomToRoomInSanByIdDto(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomInSanByIdDto")
    abstract public List<RoomInSanByIdDto> roomToRoomInSanByIdDto(List<Room> rooms);

    @Named("roomToRoomByIdDtoForUser")
    @Mapping(target = "picUrls", expression = "java( getPicUrlsFromSysFiles(room.getPics()) )")
    @Mapping(target = "name", source = "room.roomClassDic.name")
    @Mapping(target = "description", source = "room.roomClassDic.description")
    abstract public RoomByIdDto roomToRoomByIdDtoForUser(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomByIdDtoForUser")
    abstract public List<RoomByIdDto> roomToRoomByIdDtoForUser(List<Room> rooms);

    @Named("roomToRoomByIdDto")
    @Mapping(target = "picUrls", expression = "java( getPicUrlsFromSysFiles(roomClass.getPics()) )")
    abstract public RoomByIdDto roomToRoomByIdDto(RoomClass roomClass);
    @IterableMapping(qualifiedByName = "roomToRoomByIdDto")
    abstract public List<RoomByIdDto> roomToRoomByIdDto(List<RoomClass> roomClasses);


    protected String getFirstPicUrlFromSysFiles(List<SysFile> pics){
        if(pics != null && !pics.isEmpty()){
            return getPicUrlFromSysFile(pics.get(0));
        }
        return null;
    }

    @Named("roomToRoomInClassDicDto")
    @Mapping(target = "roomNumber", source = "room.roomNumber")
    @Mapping(target = "mainPic", expression = "java( room.getMainPicUrl() )")
    @Mapping(target = "price", source = "room.price")
    abstract public RoomInClassDicDto roomToRoomInClassDicDto(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomInClassDicDto")
    abstract public List<RoomInClassDicDto> roomToRoomInClassDicDto(List<Room> rooms);

    @Named("roomClassDicToDto")
    @Mapping(target = "sanId", source = "roomClassDic.san.id")
    @Mapping(target = "rooms", expression = "java( roomToRoomInClassDicDto( roomClassDic.getRooms() ) )")
    abstract public RoomClassDicDto roomClassDicToDto(RoomClassDic roomClassDic);
    @IterableMapping(qualifiedByName = "roomClassDicToDto")
    abstract public List<RoomClassDicDto> roomClassDicToDto(List<RoomClassDic> roomClassDics);
}
