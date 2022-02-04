package kz.open.sankaz.service;

import kz.open.sankaz.dto.CategoryDto;
import kz.open.sankaz.model.SanType;

import java.util.List;

public interface CategoryService extends CommonService<SanType>, CommonDtoService<SanType, CategoryDto> {
    /***
     * for Entity
     */
    List<SanType> getAllByCodeIn(List<String> codes);
    SanType getOneDto(String code); // TODO: Вынести на абстракцию


    /***
     * for DTO
     */
}
