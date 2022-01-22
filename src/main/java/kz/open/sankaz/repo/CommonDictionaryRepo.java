package kz.open.sankaz.repo;

import kz.open.sankaz.model.AbstractDictionaryEntity;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface CommonDictionaryRepo<E extends AbstractDictionaryEntity> extends CommonRepo<E> {
    List<E> getAllByName(@Param("name") String name);
    E getByCode(@Param("code") String code);
}
