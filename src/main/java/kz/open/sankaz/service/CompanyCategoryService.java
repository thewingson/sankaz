package kz.open.sankaz.service;

import kz.open.sankaz.model.CompanyCategory;
import kz.open.sankaz.pojo.dto.CompanyCategoryDto;

public interface CompanyCategoryService extends CommonDictionaryService<CompanyCategory> {
    CompanyCategory addOneDto(CompanyCategoryDto dto);
    CompanyCategory updateOneDto(Long id, CompanyCategoryDto dto);
}
