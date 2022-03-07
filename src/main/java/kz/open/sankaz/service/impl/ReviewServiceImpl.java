package kz.open.sankaz.service.impl;

import kz.open.sankaz.mapper.ReviewMapper;
import kz.open.sankaz.model.Review;
import kz.open.sankaz.pojo.dto.ReviewAvgCountDto;
import kz.open.sankaz.pojo.dto.ReviewInfoBySanIdDto;
import kz.open.sankaz.pojo.dto.ReviewRatingDto;
import kz.open.sankaz.pojo.filter.ReviewBySanIdFilter;
import kz.open.sankaz.repo.ReviewRepo;
import kz.open.sankaz.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReviewServiceImpl extends AbstractService<Review, ReviewRepo> implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;


    public ReviewServiceImpl(ReviewRepo reviewRepo) {
        super(reviewRepo);
    }

    @Override
    public List<Review> getAllBySanId(Long sanId) {
        return repo.getAllBySanId(sanId);
    }

    @Override
    public ReviewInfoBySanIdDto getReviewInfoBySanId(Long sanId, int rating) {
        float ratingStart = 0.9f;
        float ratingEnd = 5.1f;
        if(rating != 0.0f){
            ratingStart = rating;
            ratingEnd = ratingStart + 0.1f;
        }
        ReviewAvgCountDto reviewInfo = repo.getReviewInfo(sanId, 0.9f, 5.1f);
        List<ReviewRatingDto> ratingInfo = repo.getRatingInfo(sanId, 0.9f, 5.1f);
        List<Review> reviews = repo.getAllBySanIdAndRatingBetweenAndParentReviewIsNull(sanId, ratingStart, ratingEnd);
        ReviewInfoBySanIdDto info = new ReviewInfoBySanIdDto();
        info.setReviews(reviewMapper.reviewToReviewBySanIdDto(reviews));
        info.setReviewCount(reviewInfo.getCount());
        info.setAvgRating((double)reviewInfo.getAvg());
//        info.setAvgRating(reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0));
        Map<Integer, Integer> ratings = new HashMap<Integer, Integer>() {{
            put(1, 0);
            put(2, 0);
            put(3, 0);
            put(4, 0);
            put(5, 0);
        }};
//        reviews.forEach(review -> {
//            int count = ratings.getOrDefault(review.getRating().intValue(), 0);
//            ratings.put(review.getRating().intValue(), ++count);
//        });
        ratingInfo.forEach(rat -> {
            ratings.put((int) rat.getRating(), rat.getCount());
        });
        info.setRatings(ratings);
        return info;
    }

    @Override
    public List<Review> getAllByFilter(Long sanId, ReviewBySanIdFilter filter) {
        return repo.getAllBySanIdAndRatingIn(sanId, filter.getRatings());
    }

    @Override
    protected Class getCurrentClass() {
        return Review.class;
    }
}
