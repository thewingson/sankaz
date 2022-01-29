package kz.open.sankaz.service;

import kz.open.sankaz.model.BaseEntity;

import java.util.List;
import java.util.Map;

public interface CommonService<E extends BaseEntity> {

    List<E> getAllByIdIn(List<Long> ids);

    E getOne(Long id);

    E getOne(Long id, Map<String, Object> params);

    List<E> getAll();

    List<E> getAll(Map<String, Object> params);

    E addOne(E entity);

    E editOneById(E entity);

    void deleteOneById(Long id);

    void deleteOneByIdSoft(Long id);

}