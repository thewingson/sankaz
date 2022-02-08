package kz.open.sankaz.mapper;

import kz.open.sankaz.pojo.dto.ItemPicDto;
import kz.open.sankaz.model.ItemPic;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ItemPicMapper {

    @Named("itemPicToDto")
    abstract public ItemPicDto itemPicToDto(ItemPic itemPic);
    @IterableMapping(qualifiedByName = "itemPicToDto")
    abstract public List<ItemPicDto> itemPicToDto(List<ItemPic> itemPics);

    @Named("itemPicToDtoWithFile")
    @Mapping(target = "fileName",
            expression = "java(itemPic.getFile().getFileName())")
    @Mapping(target = "extension",
            expression = "java(itemPic.getFile().getExtension())")
    @Mapping(target = "size",
            expression = "java(itemPic.getFile().getSize().toString())")
    abstract public ItemPicDto itemPicToDtoWithFile(ItemPic itemPic);
    @IterableMapping(qualifiedByName = "itemPicToDtoWithFile")
    abstract public List<ItemPicDto> itemPicToDtoWithFile(List<ItemPic> itemPics);

}
