package kz.open.sankaz.repo.dictionary;

import kz.open.sankaz.model.SanType;
import kz.open.sankaz.repo.dictionary.CommonDictionaryRepo;
import org.springframework.stereotype.Repository;

@Repository
public interface SanTypeRepo extends CommonDictionaryRepo<SanType> {

//    List<SanType> getAllByCodeIn(@Param("codes") List<String> codes);
//    Optional<SanType> getByCode(@Param("code") String code); // TODO: Вынести на абстракцию

}
