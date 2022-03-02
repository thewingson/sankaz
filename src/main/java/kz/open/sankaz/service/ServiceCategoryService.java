package kz.open.sankaz.service;

import kz.open.sankaz.model.ServiceCategory;
import kz.open.sankaz.pojo.filter.DictionaryLangFilter;

public interface ServiceCategoryService extends CommonDictionaryService<ServiceCategory> {
    ServiceCategory addOne(DictionaryLangFilter filter);
    ServiceCategory updateOne(Long id, DictionaryLangFilter filter);
}
