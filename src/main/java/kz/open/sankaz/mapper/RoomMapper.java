package kz.open.sankaz.mapper;

import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.pojo.dto.RoomByIdDto;
import kz.open.sankaz.pojo.dto.RoomCreateDto;
import kz.open.sankaz.pojo.dto.RoomInSanByIdDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class RoomMapper {

    @Autowired
    protected SanMapper sanMapper;

    @Value("${application.file.upload.path.image}")
    private String APPLICATION_UPLOAD_PATH_IMAGE;

    @Named("roomToRoomCreateDto")
    @Mapping(target = "sanId", source = "room.san.id")
    @Mapping(target = "picUrls", ignore = true)
    abstract public RoomCreateDto roomToRoomCreateDto(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomCreateDto")
    abstract public List<RoomCreateDto> roomToRoomCreateDto(List<Room> rooms);

    public List<String> getPicUrlsFromRoom(Room room){
        return room.getPics().stream().map(sysFile -> APPLICATION_UPLOAD_PATH_IMAGE + sysFile.getFileName()).collect(Collectors.toList());
    }

    @Named("roomToRoomInSanByIdDto")
    @Mapping(target = "mainPicUrl", expression = "java( getFirstPicUrlFromSysFiles(room.getPics()) )")
    abstract public RoomInSanByIdDto roomToRoomInSanByIdDto(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomInSanByIdDto")
    abstract public List<RoomInSanByIdDto> roomToRoomInSanByIdDto(List<Room> rooms);

    @Named("roomToRoomByIdDto")
    @Mapping(target = "picUrls", expression = "java( getPicUrlsFromSysFiles(room.getPics()) )")
    abstract public RoomByIdDto roomToRoomByIdDto(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomByIdDto")
    abstract public List<RoomByIdDto> roomToRoomByIdDto(List<Room> rooms);


    protected String getFirstPicUrlFromSysFiles(List<SysFile> pics){
        if(pics != null && !pics.isEmpty()){
            return APPLICATION_UPLOAD_PATH_IMAGE + pics.get(0).getFileName();
        }
        return null;
    }

    protected List<String> getPicUrlsFromSysFiles(List<SysFile> pics){
        if(pics != null && !pics.isEmpty()){
            return pics.stream().map(sysFile -> APPLICATION_UPLOAD_PATH_IMAGE + sysFile.getFileName()).collect(Collectors.toList());
        }
        return null;
    }

}
