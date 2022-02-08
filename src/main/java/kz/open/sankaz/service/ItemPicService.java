package kz.open.sankaz.service;

import kz.open.sankaz.pojo.dto.ItemPicDto;
import kz.open.sankaz.model.ItemPic;

import java.io.IOException;

public interface ItemPicService extends CommonService<ItemPic>, CommonDtoService<ItemPic, ItemPicDto> {
    /***
     * for Entity
     */
    byte[] getByteArrayFromPic(ItemPic pic) throws IOException;

    /***
     * for DTO
     */
}
