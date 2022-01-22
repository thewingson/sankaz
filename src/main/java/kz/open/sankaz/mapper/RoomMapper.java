package kz.open.sankaz.mapper;

import kz.open.sankaz.dto.RoomDto;
import kz.open.sankaz.model.Review;
import kz.open.sankaz.model.Room;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class RoomMapper {

    @Autowired
    protected SanMapper sanMapper;

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

}
