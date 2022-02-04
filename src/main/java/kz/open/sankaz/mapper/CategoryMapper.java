package kz.open.sankaz.mapper;

import kz.open.sankaz.dto.CategoryDto;
import kz.open.sankaz.dto.CompanyCategoryDto;
import kz.open.sankaz.model.Category;
import kz.open.sankaz.model.CompanyCategory;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {

    @Named("categoryToDto")
    abstract public CategoryDto categoryToDto(Category category);
    @IterableMapping(qualifiedByName = "categoryToDto")
    abstract public List<CategoryDto> categoryToDto(List<Category> categories);

    @Named("companyCategoryToDto")
    abstract public CategoryDto companyCategoryToDto(CompanyCategory category);
    @IterableMapping(qualifiedByName = "companyCategoryToDto")
    abstract public List<CategoryDto> companyCategoryToDto(List<CompanyCategory> categories);

    @Named("dtoToCompanyCategory")
//    @Mapping(target = "id", ignore = true)
    abstract public CompanyCategory dtoToCompanyCategory(CompanyCategoryDto dto);
    @IterableMapping(qualifiedByName = "dtoToCompanyCategory")
    abstract public List<CompanyCategory> dtoToCompanyCategory(List<CompanyCategoryDto> dtos);

}
