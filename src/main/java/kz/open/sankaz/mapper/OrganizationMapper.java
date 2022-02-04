package kz.open.sankaz.mapper;

import kz.open.sankaz.dto.OrganizationDto;
import kz.open.sankaz.model.Organization;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class OrganizationMapper {

    @Value("${application.file.upload.path}")
    private String APPLICATION_UPLOAD_PATH;

    @Named("organizationToDto")
    @Mapping(target = "user", ignore = true)
    abstract public OrganizationDto organizationToDto(Organization organization);
    @IterableMapping(qualifiedByName = "organizationToDto")
    abstract public List<OrganizationDto> organizationToDto(List<Organization> organizations);

    // with additional data
    @Named("organizationToDtoWithAddData")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "picUrls", expression = "java(getPicUrlsFromOrganization(organization))")
    abstract public OrganizationDto organizationToDtoWithAddData(Organization organization);
    @IterableMapping(qualifiedByName = "organizationToDtoWithAddData")
    abstract public List<OrganizationDto> organizationToDtoWithAddData(List<Organization> organizations);

    public List<String> getPicUrlsFromOrganization(Organization organization){
        return organization.getPics().stream().map(sysFile -> APPLICATION_UPLOAD_PATH + sysFile.getFileName()).collect(Collectors.toList());
    }

}
