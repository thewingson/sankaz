package kz.open.sankaz.service;

import kz.open.sankaz.dto.ReviewDto;
import kz.open.sankaz.model.Review;

import java.util.List;

public interface ReviewService extends CommonService<Review>, CommonDtoService<Review, ReviewDto> {
    /***
     * for Entity
     */
    List<Review> getAllByIdIn(List<Long> ids);

    /***
     * for DTO
     */
    Review addDto(Long id, ReviewDto reviewDto);
    List<Review> addDto(Long id, List<ReviewDto> reviewDtos);
}
