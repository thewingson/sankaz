package kz.open.sankaz.service;

import kz.open.sankaz.model.SanType;
import kz.open.sankaz.pojo.dto.SanTypeDto;

public interface SanTypeService extends CommonDictionaryService<SanType> {
    SanType addOneDto(SanTypeDto sanTypeDto);
    SanType updateOneDto(Long id, SanTypeDto sanTypeDto);
}
