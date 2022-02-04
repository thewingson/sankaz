package kz.open.sankaz.repo;

import kz.open.sankaz.model.AbstractDictionaryLangEntity;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface CommonDictionaryLangRepo<E extends AbstractDictionaryLangEntity> extends CommonRepo<E> {
    List<E> getAllByNameKz(@Param("nameKz") String nameKz);
}
