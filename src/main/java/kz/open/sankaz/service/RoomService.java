package kz.open.sankaz.service;

import kz.open.sankaz.model.Room;
import kz.open.sankaz.pojo.dto.RoomDto;

public interface RoomService extends CommonService<Room> {
    Room addOneDto(RoomDto roomDto);
    Room updateOneDto(Long id, RoomDto roomDto);
}
