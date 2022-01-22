package kz.open.sankaz.repo;

import kz.open.sankaz.model.Review;
import kz.open.sankaz.model.San;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends CommonRepo<Review> {

    List<Review> getAllBySan(@Param("san") San san);
}
