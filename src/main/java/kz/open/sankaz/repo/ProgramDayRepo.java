package kz.open.sankaz.repo;

import kz.open.sankaz.model.ProgramDay;
import kz.open.sankaz.model.TourProgram;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramDayRepo extends CommonRepo<ProgramDay> {

    List<ProgramDay> getAllByProgram(@Param("program") TourProgram program);

}
