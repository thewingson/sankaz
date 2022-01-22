package kz.open.sankaz.repo;

import kz.open.sankaz.model.ProgramService;
import kz.open.sankaz.model.TourProgram;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramServiceRepo extends CommonRepo<ProgramService> {

    List<ProgramService> getAllByProgram(@Param("program") TourProgram program);

}
