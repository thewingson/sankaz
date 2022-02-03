package kz.open.sankaz.mapper;

import kz.open.sankaz.dto.OrganizationDto;
import kz.open.sankaz.model.Organization;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class OrganizationMapper {

    @Named("organizationToDto")
    @Mapping(target = "user", ignore = true)
    abstract public OrganizationDto organizationToDto(Organization organization);
    @IterableMapping(qualifiedByName = "organizationToDto")
    abstract public List<OrganizationDto> organizationToDto(List<Organization> organizations);

}
