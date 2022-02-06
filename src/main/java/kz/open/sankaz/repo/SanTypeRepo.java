package kz.open.sankaz.repo;

import kz.open.sankaz.model.SanType;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanTypeRepo extends CommonDictionaryRepo<SanType> {

//    List<SanType> getAllByCodeIn(@Param("codes") List<String> codes);
//    Optional<SanType> getByCode(@Param("code") String code); // TODO: Вынести на абстракцию

}
