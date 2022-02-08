package kz.open.sankaz.service;

import kz.open.sankaz.pojo.dto.BaseDto;
import kz.open.sankaz.model.BaseEntity;

import java.util.List;
import java.util.Map;

public interface CommonDtoService<E extends BaseEntity, D extends BaseDto> {

    D getOneDto(Long id);
    List<D> getAllDto();
    List<D> getAllDto(Map<String, Object> params);

    E addOneDto(D dto);

    E updateOneDto(Long id, D dto);
    E updateOneDto(Map<String, Object> params, D dto);

}