package kz.open.sankaz.mapper;

import kz.open.sankaz.model.Organization;
import kz.open.sankaz.pojo.dto.OrganizationDto;
import kz.open.sankaz.pojo.dto.OrganizationRegisterDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class OrganizationMapper extends AbstractMapper {

    @Autowired
    protected SecUserMapper secUserMapper;

    @Autowired
    protected SanMapper sanMapper;

    @Named("organizationToDto")
    @Mapping(target = "user", expression = "java( secUserMapper.userToSecUserInOrgDto(organization.getUser()) )")
   // @Mapping(target = "pics", expression = "java( fileToDto(organization.getPics()) )")
    @Mapping(target = "companyCategory", expression = "java( dictionaryToDto(organization.getCompanyCategory()) )")
    abstract public OrganizationDto organizationToDto(Organization organization);
    @IterableMapping(qualifiedByName = "organizationToDto")
    abstract public List<OrganizationDto> organizationToDto(List<Organization> organizations);

    // with additional data
    @Named("organizationToDtoWithAddData")
    @Mapping(target = "user", ignore = true)
   // @Mapping(target = "pics", expression = "java( fileToDto( organization.getPics() ) )")
    abstract public OrganizationDto organizationToDtoWithAddData(Organization organization);
    @IterableMapping(qualifiedByName = "organizationToDtoWithAddData")
    abstract public List<OrganizationDto> organizationToDtoWithAddData(List<Organization> organizations);

    @Named("organizationToOrganizationRegisterDto")
    @Mapping(target = "orgId", source = "organization.id")
    @Mapping(target = "sans", expression = "java( sanMapper.sanToSanForMainDto(organization.getSans()) )")
    abstract public OrganizationRegisterDto toOrganizationRegisterDto(Organization organization);
    @IterableMapping(qualifiedByName = "organizationToOrganizationRegisterDto")
    abstract public List<OrganizationRegisterDto> toOrganizationRegisterDto(List<Organization> organizations);

}
