package kz.open.sankaz.service;

import kz.open.sankaz.model.BaseEntity;

import java.util.List;

public interface CommonService<E extends BaseEntity> {

    List<E> getAllByIdIn(List<Long> ids);

    E getOne(Long id);

    List<E> getAll();

    E addOne(E entity);

    E editOneById(E entity);

    void deleteOneById(Long id);

}