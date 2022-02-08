package kz.open.sankaz.mapper;

import kz.open.sankaz.pojo.dto.RoomCreateDto;
import kz.open.sankaz.pojo.dto.RoomDto;
import kz.open.sankaz.model.Room;
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

    @Value("${application.file.upload.path}")
    private String APPLICATION_UPLOAD_PATH;

    @Named("roomToDto")
    @Mapping(target = "san", ignore = true)
    abstract public RoomDto roomToDto(Room room);
    @IterableMapping(qualifiedByName = "roomToDto")
    abstract public List<RoomDto> roomToDto(List<Room> rooms);

    @Named("roomToDtoWithSan")
    @Mapping(target = "san",
            expression = "java(sanMapper.sanToDto(room.getSan()))")
    abstract public RoomDto roomToDtoWithSan(Room room);
    @IterableMapping(qualifiedByName = "roomToDtoWithSan")
    abstract public List<RoomDto> roomToDtoWithSan(List<Room> rooms);

    @Named("roomToRoomCreateDto")
    @Mapping(target = "sanId", source = "room.san.id")
    @Mapping(target = "picUrls", ignore = true)
    abstract public RoomCreateDto roomToRoomCreateDto(Room room);
    @IterableMapping(qualifiedByName = "roomToRoomCreateDto")
    abstract public List<RoomCreateDto> roomToRoomCreateDto(List<Room> rooms);

    public List<String> getPicUrlsFromRoom(Room room){
        return room.getPics().stream().map(sysFile -> APPLICATION_UPLOAD_PATH + sysFile.getFileName()).collect(Collectors.toList());
    }

}
