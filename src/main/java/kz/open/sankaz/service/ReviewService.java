package kz.open.sankaz.service;

import kz.open.sankaz.model.Review;
import kz.open.sankaz.pojo.dto.ReviewInfoBySanIdDto;
import kz.open.sankaz.pojo.filter.ReviewBySanIdFilter;

import java.util.List;

public interface ReviewService extends CommonService<Review> {
    List<Review> getAllByIdIn(List<Long> ids);
    List<Review> getAllBySanId(Long sanId);
    ReviewInfoBySanIdDto getReviewInfoBySanId(Long sanId, int rating);
    List<Review> getAllByFilter(Long sanId, ReviewBySanIdFilter filter);
}
