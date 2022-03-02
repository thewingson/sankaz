package kz.open.sankaz.service;

import kz.open.sankaz.model.Gender;
import kz.open.sankaz.pojo.filter.DictionaryLangFilter;

public interface GenderService extends CommonDictionaryLangService<Gender>{
    Gender updateOne(Long id, DictionaryLangFilter filter);
    Gender addOne(DictionaryLangFilter filter);
}
