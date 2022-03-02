package kz.open.sankaz.service;

import kz.open.sankaz.model.City;
import kz.open.sankaz.pojo.filter.DictionaryLangFilter;

public interface CityService extends CommonDictionaryLangService<City>{
    City updateOne(Long id, DictionaryLangFilter filter);
    City addOne(DictionaryLangFilter filter);
}
