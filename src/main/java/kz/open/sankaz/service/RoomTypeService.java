package kz.open.sankaz.service;

import kz.open.sankaz.pojo.dto.RoomTypeDto;
import kz.open.sankaz.model.RoomType;

public interface RoomTypeService extends CommonDictionaryService<RoomType> {
    RoomType addOneDto(RoomTypeDto dto);
    RoomType updateOneDto(Long id, RoomTypeDto dto);
}
