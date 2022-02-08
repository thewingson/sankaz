package kz.open.sankaz.mapper;

import kz.open.sankaz.pojo.dto.HyperLinkDto;
import kz.open.sankaz.pojo.dto.HyperLinkTypeDto;
import kz.open.sankaz.model.HyperLink;
import kz.open.sankaz.model.HyperLinkType;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class HyperLinkMapper {

    @Named("hyperLinkToDto")
    abstract public HyperLinkDto hyperLinkToDto(HyperLink hyperLink);
    @IterableMapping(qualifiedByName = "hyperLinkToDto")
    abstract public List<HyperLinkDto> hyperLinkToDto(List<HyperLink> hyperLinks);

    @Named("hyperLinkTypeToDto")
    abstract public HyperLinkTypeDto hyperLinkTypeToDto(HyperLinkType linkType);
    @IterableMapping(qualifiedByName = "hyperLinkTypeToDto")
    abstract public List<HyperLinkTypeDto> hyperLinkTypeToDto(List<HyperLinkType> linkTypes);

    @Named("hyperLinkToDtoWithType")
    @Mapping(target = "linkTypeCode", source = "hyperLink.linkType.code")
    abstract public HyperLinkDto hyperLinkToDtoWithType(HyperLink hyperLink);
    @IterableMapping(qualifiedByName = "hyperLinkToDtoWithType")
    abstract public List<HyperLinkDto> hyperLinkToDtoWithType(List<HyperLink> hyperLinks);

}
