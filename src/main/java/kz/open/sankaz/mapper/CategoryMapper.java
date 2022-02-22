package kz.open.sankaz.mapper;

import kz.open.sankaz.model.CompanyCategory;
import kz.open.sankaz.model.SanType;
import kz.open.sankaz.model.ServiceCategory;
import kz.open.sankaz.pojo.dto.CompanyCategoryDto;
import kz.open.sankaz.pojo.dto.SanTypeDto;
import kz.open.sankaz.pojo.dto.ServiceCategoryDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {

    /**
     * from Entity to DTO
     * */
    @Named("sanTypeToDto")
    abstract public SanTypeDto sanTypeToDto(SanType sanType);
    @IterableMapping(qualifiedByName = "sanTypeToDto")
    abstract public List<SanTypeDto> sanTypeToDto(List<SanType> sanTypes);

    /**
     * from DTO to Entity
     * */
    @Named("dtoToCompanyCategory")
    abstract public CompanyCategory dtoToCompanyCategory(CompanyCategoryDto dto);
    @IterableMapping(qualifiedByName = "dtoToCompanyCategory")
    abstract public List<CompanyCategory> dtoToCompanyCategory(List<CompanyCategoryDto> dtos);

    @Named("dtoToServiceCategory")
    abstract public ServiceCategory dtoToServiceCategory(ServiceCategoryDto dto);
    @IterableMapping(qualifiedByName = "dtoToServiceCategory")
    abstract public List<ServiceCategory> dtoToServiceCategory(List<ServiceCategoryDto> dtos);

}
