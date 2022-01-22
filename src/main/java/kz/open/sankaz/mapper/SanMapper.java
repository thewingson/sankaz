package kz.open.sankaz.mapper;

import kz.open.sankaz.dto.SanDto;
import kz.open.sankaz.model.San;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SanMapper {

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
    protected HyperLinkMapper hyperLinkMapper;
    @Lazy
    @Autowired
    protected ItemPicMapper itemPicMapper;

    @Named("sanToDto")
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "reviews", ignore = true)
//    @Mapping(target = "links", ignore = true)
//    @Mapping(target = "pics", ignore = true)
    abstract public SanDto sanToDto(San san);
    @IterableMapping(qualifiedByName = "sanToDto")
    abstract public List<SanDto> sanToDto(List<San> sans);

    @Named("sanToDtoWithAll")
    @Mapping(target = "categories",
            expression = "java(categoryMapper.categoryToDto(san.getCategories()))")
    @Mapping(target = "rooms",
            expression = "java(roomMapper.roomToDto(san.getRooms()))")
    @Mapping(target = "reviews",
            expression = "java(reviewMapper.reviewToDto(san.getReviews()))")
//    @Mapping(target = "links",
//            expression = "java(hyperLinkMapper.hyperLinkToDto(san.getLinks()))")
//    @Mapping(target = "pics",
//            expression = "java(itemPicMapper.itemPicToDto(san.getPics()))")
    abstract public SanDto sanToDtoWithAll(San san);
    @IterableMapping(qualifiedByName = "sanToDtoWithAll")
    abstract public List<SanDto> sanToDtoWithAll(List<San> sans);

}
