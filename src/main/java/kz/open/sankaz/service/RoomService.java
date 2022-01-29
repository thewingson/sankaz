package kz.open.sankaz.service;

import kz.open.sankaz.dto.RoomDto;
import kz.open.sankaz.model.Room;

import java.util.List;

public interface RoomService extends CommonService<Room>, CommonDtoService<Room, RoomDto> {

    /***
     * for Entity
     */

    /***
     * for DTO
     */
    Room addDto(Long sanId, RoomDto roomDto);
    List<Room> addDto(Long sanId, List<RoomDto> roomDtos);
}
