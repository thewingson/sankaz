package kz.open.sankaz.service;

import kz.open.sankaz.model.BaseEntity;
import kz.open.sankaz.pojo.dto.PageDto;
import org.springframework.data.domain.Page;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CommonService<E extends BaseEntity> {

    List<E> getAllByIdIn(List<Long> ids);

    E getOne(Long id);

    E getOne(Long id, Map<String, Object> params);

    List<E> getAll();

    Page<E> getAll(int page, int size);

    List<E> getAll(Map<String, Object> params);

    E addOne(E entity);

    E editOneById(E entity);

    List<E> saveAll(List<E> entities);

    void deleteOneById(Long id) throws SQLException;

    void deleteOneByIdSoft(Long id);

    PageDto getAllPage(int page, int size);

}