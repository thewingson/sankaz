package kz.open.sankaz.service;

import kz.open.sankaz.model.AbstractDictionaryEntity;

import java.util.List;

public interface CommonDictionaryService<E extends AbstractDictionaryEntity> extends CommonService<E> {

    List<E> getAllByCodeIn(List<String> codes);

    E getOneByCode(String code);

}