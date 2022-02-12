package kz.open.sankaz.service;

import kz.open.sankaz.model.ItemPic;
import kz.open.sankaz.pojo.dto.ItemPicDto;

public interface ItemPicService extends CommonService<ItemPic> {
    ItemPic addOneDto(ItemPicDto picDto);
}
