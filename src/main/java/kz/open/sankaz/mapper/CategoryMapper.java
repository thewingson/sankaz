package kz.open.sankaz.mapper;

import kz.open.sankaz.dto.CategoryDto;
import kz.open.sankaz.model.Category;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {

    @Named("categoryToDto")
    abstract public CategoryDto categoryToDto(Category category);
    @IterableMapping(qualifiedByName = "categoryToDto")
    abstract public List<CategoryDto> categoryToDto(List<Category> categories);

}
