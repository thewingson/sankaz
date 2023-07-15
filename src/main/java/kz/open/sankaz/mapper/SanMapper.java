package kz.open.sankaz.mapper;

import kz.open.sankaz.model.San;
import kz.open.sankaz.pojo.dto.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SanMapper extends AbstractMapper {

    @Lazy
    @Autowired
    protected ReviewMapper reviewMapper;
    @Lazy
    @Autowired
    protected CategoryMapper categoryMapper;
    @Lazy
    @Autowired
    protected RoomMapper roomMapper;

    @Named("sanToAdminDto")
    @Mapping(target = "sanType", expression = "java( dictionaryToDto(san.getSanType()) )")
    @Mapping(target = "city", expression = "java( dictionaryToDto(san.getCity()) )")
    @Mapping(target = "orgId", source = "san.organization.id")
    @Mapping(target = "telNumbers", expression = "java( getTelNumberValuesFromEntity(san.getTelNumbers()) )")
   // @Mapping(target = "pics", expression = "java( fileToDto(san.getPics()) )")
    abstract public SanAdminDto sanToAdminDto(San san);
  //  @IterableMapping(qualifiedByName = "sanToAdminDto")
    abstract public List<SanAdminDto> sanToAdminDto(List<San> sans);

    @Named("sanToDto")
    @Mapping(target = "sanTypeId", source = "san.sanType.id")
    @Mapping(target = "orgId", source = "san.organization.id")
    abstract public SanDto sanToDto(San san);
    @IterableMapping(qualifiedByName = "sanToDto")
    @Mapping(target = "instagramLink", expression = "java(getInstaDefault())")
    abstract public List<SanDto> sanToDto(List<San> sans);

    @Named("sanToSanForMainDto")
    //TODO @Mapping(target = "mainPicUrl", expression = "java(getPicUrlFromSysFile(san.getMainPic()))")
    @Mapping(target = "rating", source = "san.rating")
    @Mapping(target = "sanType", expression = "java( dictionaryToDto(san.getSanType()) )")
    @Mapping(target = "reviewCount", source = "san.reviewCount")
    @Mapping(target = "telNumbers", expression = "java( getTelNumberValuesFromEntity(san.getTelNumbers()) )")
    @Mapping(target = "instagramLink", expression = "java(getInstaDefault())")
    abstract public SanForMainDto sanToSanForMainDto(San san);
    @IterableMapping(qualifiedByName = "sanToSanForMainDto")
    abstract public List<SanForMainDto> sanToSanForMainDto(List<San> sans);

    @Named("sanToSanForMainAdminDto")
    //TODO@Mapping(target = "mainPicUrl", expression = "java(getPicUrlFromSysFile(san.getMainPic()))")
    @Mapping(target = "rating", source = "san.rating")
    @Mapping(target = "sanType", expression = "java( dictionaryToDto(san.getSanType()) )")
    @Mapping(target = "city", expression = "java( dictionaryToDto(san.getCity()) )")
    @Mapping(target = "address", source = "san.address")
    @Mapping(target = "reviewCount", source = "san.reviewCount")
    @Mapping(target = "telNumbers", expression = "java( getTelNumberValuesFromEntity(san.getTelNumbers()) )")
    @Mapping(target = "instagramLink", expression = "java(getInstaDefault())")
    abstract public SanForMainAdminDto sanToSanForMainAdminDto(San san);
    @IterableMapping(qualifiedByName = "sanToSanForMainAdminDto")
    abstract public List<SanForMainAdminDto> sanToSanForMainAdminDto(List<San> sans);

    @Named("sanToSanByIdDto")
    @Mapping(target = "rating", source = "san.rating")
    @Mapping(target = "sanType", source = "san.sanType.id")
    @Mapping(target = "telNumbers", expression = "java( getTelNumberValuesFromEntity(san.getTelNumbers()) )")
    @Mapping(target = "reviewCount", source = "san.reviewCount")
    @Mapping(target = "rooms", expression = "java( roomMapper.roomToRoomInSanByIdDto(san.getRooms()) )")
    @Mapping(target = "instagramLink", expression = "java(getInstaDefault())")
    abstract public SanByIdDto sanToSanByIdDto(San san);
    @IterableMapping(qualifiedByName = "sanToSanByIdDto")
    abstract public List<SanByIdDto> sanToSanByIdDto(List<San> sans);

    @Named("sanToSanSimpleDto")
    //@Mapping(target = "mainPicUrl", expression = "java(getPicUrlFromSysFile(san.getMainPic()))")
    abstract public SanSimpleDto sanToSanSimpleDto(San san);
    @IterableMapping(qualifiedByName = "sanToSanSimpleDto")
    abstract public List<SanSimpleDto> sanToSanSimpleDto(List<San> sans);

}
