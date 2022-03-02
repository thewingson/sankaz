package kz.open.sankaz.service;

import kz.open.sankaz.model.SanType;
import kz.open.sankaz.pojo.filter.DictionaryLangFilter;

public interface SanTypeService extends CommonDictionaryService<SanType> {
    SanType addOne(DictionaryLangFilter filter);
    SanType updateOne(Long id, DictionaryLangFilter filter);
}
