package kz.open.sankaz.mapper;

import kz.open.sankaz.model.Organization;
import kz.open.sankaz.pojo.dto.OrganizationDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class OrganizationMapper extends AbstractMapper {

    @Named("organizationToDto")
    @Mapping(target = "user", ignore = true)
    abstract public OrganizationDto organizationToDto(Organization organization);
    @IterableMapping(qualifiedByName = "organizationToDto")
    abstract public List<OrganizationDto> organizationToDto(List<Organization> organizations);

    // with additional data
    @Named("organizationToDtoWithAddData")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "picUrls", expression = "java( getPicUrlsFromSysFiles( organization.getPics() ) )")
    abstract public OrganizationDto organizationToDtoWithAddData(Organization organization);
    @IterableMapping(qualifiedByName = "organizationToDtoWithAddData")
    abstract public List<OrganizationDto> organizationToDtoWithAddData(List<Organization> organizations);

}
