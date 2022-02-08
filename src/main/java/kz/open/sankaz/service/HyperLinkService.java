package kz.open.sankaz.service;

import kz.open.sankaz.pojo.dto.HyperLinkDto;
import kz.open.sankaz.model.HyperLink;

import java.util.List;

public interface HyperLinkService extends CommonService<HyperLink>, CommonDtoService<HyperLink, HyperLinkDto> {
    /***
     * for Entity
     */
    List<HyperLink> getAllByIdIn(List<Long> ids);

    /***
     * for DTO
     */
}
