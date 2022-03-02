package kz.open.sankaz.service;

import kz.open.sankaz.model.CompanyCategory;
import kz.open.sankaz.pojo.filter.DictionaryLangFilter;

public interface CompanyCategoryService extends CommonDictionaryService<CompanyCategory> {
    CompanyCategory addOne(DictionaryLangFilter filter);
    CompanyCategory updateOne(Long id, DictionaryLangFilter filter);
}
