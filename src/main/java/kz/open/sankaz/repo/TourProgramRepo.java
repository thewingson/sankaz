package kz.open.sankaz.repo;

import kz.open.sankaz.model.Tour;
import kz.open.sankaz.model.TourProgram;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourProgramRepo extends CommonRepo<TourProgram> {

    List<TourProgram> getAllByTour(@Param("tour") Tour tour);

}
