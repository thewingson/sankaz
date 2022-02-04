package kz.open.sankaz.mapper;

import kz.open.sankaz.dto.CategoryDto;
import kz.open.sankaz.dto.CompanyCategoryDto;
import kz.open.sankaz.dto.ServiceCategoryDto;
import kz.open.sankaz.model.CompanyCategory;
import kz.open.sankaz.model.SanType;
import kz.open.sankaz.model.ServiceCategory;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {

    @Named("sanTypeToDto")
    abstract public CategoryDto sanTypeToDto(SanType sanType);
    @IterableMapping(qualifiedByName = "sanTypeToDto")
    abstract public List<CategoryDto> sanTypeToDto(List<SanType> sanTypes);

    @Named("companyCategoryToDto")
    abstract public CompanyCategoryDto companyCategoryToDto(CompanyCategory category);
    @IterableMapping(qualifiedByName = "companyCategoryToDto")
    abstract public List<CompanyCategoryDto> companyCategoryToDto(List<CompanyCategory> categories);

    @Named("serviceCategoryToDto")
    abstract public ServiceCategoryDto serviceCategoryToDto(ServiceCategory category);
    @IterableMapping(qualifiedByName = "serviceCategoryToDto")
    abstract public List<ServiceCategoryDto> serviceCategoryToDto(List<ServiceCategory> categories);

    @Named("dtoToCompanyCategory")
    abstract public CompanyCategory dtoToCompanyCategory(CompanyCategoryDto dto);
    @IterableMapping(qualifiedByName = "dtoToCompanyCategory")
    abstract public List<CompanyCategory> dtoToCompanyCategory(List<CompanyCategoryDto> dtos);

    @Named("dtoToServiceCategory")
    abstract public ServiceCategory dtoToServiceCategory(ServiceCategoryDto dto);
    @IterableMapping(qualifiedByName = "dtoToServiceCategory")
    abstract public List<ServiceCategory> dtoToServiceCategory(List<ServiceCategoryDto> dtos);

}
