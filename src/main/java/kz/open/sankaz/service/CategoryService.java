package kz.open.sankaz.service;

import kz.open.sankaz.dto.CategoryDto;
import kz.open.sankaz.model.Category;

import java.util.List;

public interface CategoryService extends CommonService<Category>, CommonDtoService<Category, CategoryDto> {
    /***
     * for Entity
     */
    List<Category> getAllByCodeIn(List<String> codes);
    Category getOneDto(String code); // TODO: Вынести на абстракцию


    /***
     * for DTO
     */
}
