package kz.open.sankaz.repo;

import kz.open.sankaz.model.Review;
import kz.open.sankaz.model.San;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends CommonRepo<Review> {

    List<Review> getAllBySan(@Param("san") San san);

    List<Review> getAllByDeletedByIsNullAndSanId(@Param("san") Long sanId);

//    @Query("SELECT e FROM Review e WHERE e.san.id = :sanId and e.rating IN :ratings")
    List<Review> getAllByDeletedByIsNullAndSanIdAndRatingIn(@Param("sanId") Long sanId, @Param("ratings") List<Float> ratings);
}
