package kz.open.sankaz.repo.dictionary;

import kz.open.sankaz.model.AbstractDictionaryLangEntity;
import kz.open.sankaz.repo.CommonRepo;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface CommonLangDictionaryRepo<E extends AbstractDictionaryLangEntity> extends CommonRepo<E> {
    List<E> getAllByNameKz(@Param("name") String name);
}
