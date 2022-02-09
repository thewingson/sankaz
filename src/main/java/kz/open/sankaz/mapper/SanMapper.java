package kz.open.sankaz.mapper;

import kz.open.sankaz.model.San;
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
    @Lazy
    @Autowired
    protected ItemPicMapper itemPicMapper;

    @Named("sanToDto")
    @Mapping(target = "sanTypes", ignore = true)
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

}
