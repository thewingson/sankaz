package kz.open.sankaz.repo;

import kz.open.sankaz.model.Review;
import kz.open.sankaz.model.San;
import kz.open.sankaz.pojo.dto.ReviewAvgCountDto;
import kz.open.sankaz.pojo.dto.ReviewRatingDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends CommonRepo<Review> {

    List<Review> getAllBySan(@Param("san") San san);

    List<Review> getAllBySanId(@Param("san") Long sanId);

    List<Review> getAllBySanIdAndRatingBetweenAndParentReviewIsNull(@Param("san") Long sanId,
                                                @Param("ratingStart") Float ratingStart,
                                                @Param("ratingEnd") Float ratingEnd);

    @Query(nativeQuery = true)
    ReviewAvgCountDto getReviewInfo(@Param("sanId") Long sanId,
                                    @Param("ratingStart") Float ratingStart,
                                    @Param("ratingEnd") Float ratingEnd);

    @Query(nativeQuery = true)
    List<ReviewRatingDto> getRatingInfo(@Param("sanId") Long sanId,
                                  @Param("ratingStart") Float ratingStart,
                                  @Param("ratingEnd") Float ratingEnd);

//    @Query("SELECT e FROM Review e WHERE e.san.id = :sanId and e.rating IN :ratings")
    List<Review> getAllBySanIdAndRatingIn(@Param("sanId") Long sanId, @Param("ratings") List<Float> ratings);
}
