package kz.open.sankaz.repo.dictionary;

import kz.open.sankaz.model.AbstractDictionaryEntity;
import kz.open.sankaz.repo.CommonRepo;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface CommonDictionaryRepo<E extends AbstractDictionaryEntity> extends CommonRepo<E> {
    List<E> getAllByName(@Param("name") String name);
    List<E> getAllByCodeIn(@Param("codes") List<String> codes);
    Optional<E> getByCode(@Param("code") String code);
}
