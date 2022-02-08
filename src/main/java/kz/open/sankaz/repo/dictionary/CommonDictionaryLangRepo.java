package kz.open.sankaz.repo.dictionary;

import kz.open.sankaz.model.AbstractDictionaryLangEntity;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface CommonDictionaryLangRepo<E extends AbstractDictionaryLangEntity> extends CommonDictionaryRepo<E> {
    List<E> getAllByNameKz(@Param("nameKz") String nameKz);
}
