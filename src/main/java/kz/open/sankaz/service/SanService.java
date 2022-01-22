package kz.open.sankaz.service;

import kz.open.sankaz.dto.HyperLinkDto;
import kz.open.sankaz.dto.ReviewDto;
import kz.open.sankaz.dto.RoomDto;
import kz.open.sankaz.dto.SanDto;
import kz.open.sankaz.model.San;

import java.util.List;

public interface SanService extends CommonService<San>, CommonDtoService<San, SanDto> {

    SanDto getOneDto(Long id);
    List<SanDto> getAllDto();

    San addRoomsDto(Long id, List<RoomDto> roomDtos);
    San addReviewsDto(Long id, List<ReviewDto> reviewDtos);
    San addHyperLinksDto(Long id, List<HyperLinkDto> hyperLinkDtos);
}
