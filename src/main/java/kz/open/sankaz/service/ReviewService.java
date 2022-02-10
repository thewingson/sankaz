package kz.open.sankaz.service;

import kz.open.sankaz.pojo.dto.ReviewDto;
import kz.open.sankaz.model.Review;
import kz.open.sankaz.pojo.filter.ReviewBySanIdFilter;

import java.util.List;

public interface ReviewService extends CommonService<Review>, CommonDtoService<Review, ReviewDto> {
    /***
     * for Entity
     */
    List<Review> getAllByIdIn(List<Long> ids);
    List<Review> getAllBySanId(Long sanId);

    /***
     * for DTO
     */
    Review addDto(Long id, ReviewDto reviewDto);
    List<Review> addDto(Long id, List<ReviewDto> reviewDtos);

    List<Review> getAllByFilter(Long sanId, ReviewBySanIdFilter filter);
}
