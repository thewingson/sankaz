package kz.open.sankaz.service;

import kz.open.sankaz.model.AbstractDictionaryLangEntity;
import kz.open.sankaz.pojo.filter.DictionaryLangFilter;

public interface CommonDictionaryLangService<E extends AbstractDictionaryLangEntity> extends CommonService<E> {
    E updateOne(Long id, DictionaryLangFilter filter);
    E addOne(DictionaryLangFilter filter);
}