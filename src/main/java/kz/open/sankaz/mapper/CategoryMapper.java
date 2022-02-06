package kz.open.sankaz.mapper;

import kz.open.sankaz.dto.SanTypeDto;
import kz.open.sankaz.dto.CompanyCategoryDto;
import kz.open.sankaz.dto.RoomTypeDto;
import kz.open.sankaz.dto.ServiceCategoryDto;
import kz.open.sankaz.model.CompanyCategory;
import kz.open.sankaz.model.RoomType;
import kz.open.sankaz.model.SanType;
import kz.open.sankaz.model.ServiceCategory;
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

    @Named("companyCategoryToDto")
    abstract public CompanyCategoryDto companyCategoryToDto(CompanyCategory category);
    @IterableMapping(qualifiedByName = "companyCategoryToDto")
    abstract public List<CompanyCategoryDto> companyCategoryToDto(List<CompanyCategory> categories);

    @Named("serviceCategoryToDto")
    abstract public ServiceCategoryDto serviceCategoryToDto(ServiceCategory category);
    @IterableMapping(qualifiedByName = "serviceCategoryToDto")
    abstract public List<ServiceCategoryDto> serviceCategoryToDto(List<ServiceCategory> categories);

    @Named("roomTypeToDto")
    abstract public RoomTypeDto roomTypeToDto(RoomType roomType);
    @IterableMapping(qualifiedByName = "roomTypeToDto")
    abstract public List<RoomTypeDto> roomTypeToDto(List<RoomType> roomTypes);

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

    @Named("dtoToRoomType")
    abstract public RoomType dtoToRoomType(RoomTypeDto dto);
    @IterableMapping(qualifiedByName = "dtoToRoomType")
    abstract public List<RoomType> dtoToRoomType(List<RoomTypeDto> dtos);

}
