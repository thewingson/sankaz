package kz.open.sankaz.mapper;

import kz.open.sankaz.model.San;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.model.TelNumber;
import kz.open.sankaz.pojo.dto.SanByIdDto;
import kz.open.sankaz.pojo.dto.SanDto;
import kz.open.sankaz.pojo.dto.SanForMainDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class SanMapper {

    @Value("${application.file.upload.path}")
    private String APPLICATION_UPLOAD_PATH;

    @Lazy
    @Autowired
    protected ReviewMapper reviewMapper;
    @Lazy
    @Autowired
    protected CategoryMapper categoryMapper;
    @Lazy
    @Autowired
    protected RoomMapper roomMapper;

    @Named("sanToDto")
    @Mapping(target = "sanTypeId", source = "san.sanType.id")
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    abstract public SanDto sanToDto(San san);
    @IterableMapping(qualifiedByName = "sanToDto")
    abstract public List<SanDto> sanToDto(List<San> sans);

    @Named("sanToSanForMainDto")
    @Mapping(target = "picUrl", expression = "java(getPicFullUrl(san.getMainPicUrl()))")
    @Mapping(target = "rating", source = "san.rating")
    @Mapping(target = "reviewCount", source = "san.reviewCount")
    abstract public SanForMainDto sanToSanForMainDto(San san);
    @IterableMapping(qualifiedByName = "sanToSanForMainDto")
    abstract public List<SanForMainDto> sanToSanForMainDto(List<San> sans);

    protected String getPicFullUrl(String picUrl){
        if(picUrl == null){
            return null;
        }
        return APPLICATION_UPLOAD_PATH + picUrl;
    }

    @Named("sanToSanByIdDto")
    @Mapping(target = "mainPicUrl", expression = "java( getPicUrlFromSysFile(san.getPic()) )")
    @Mapping(target = "rating", source = "san.rating")
    @Mapping(target = "sanType", source = "san.sanType.id")
    @Mapping(target = "geoLink", ignore = true)
    @Mapping(target = "telNumbers", expression = "java( getTelNumberValuesFromEntity(san.getTelNumbers()) )")
    @Mapping(target = "reviewCount", source = "san.reviewCount")
    @Mapping(target = "rooms", expression = "java( roomMapper.roomToRoomInSanByIdDto(san.getRooms()) )")
    abstract public SanByIdDto sanToSanByIdDto(San san);
    @IterableMapping(qualifiedByName = "sanToSanByIdDto")
    abstract public List<SanByIdDto> sanToSanByIdDto(List<San> sans);

    protected String getPicUrlFromSysFile(SysFile file){
        if(file != null){
            return APPLICATION_UPLOAD_PATH + file.getFileName();
        }
        return null;
    }

    protected List<String> getTelNumberValuesFromEntity(List<TelNumber> telNumbers){
        return telNumbers.stream().map(TelNumber::getValue).collect(Collectors.toList());
    }

}
