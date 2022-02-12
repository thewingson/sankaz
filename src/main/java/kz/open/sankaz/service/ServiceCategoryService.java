package kz.open.sankaz.service;

import kz.open.sankaz.model.ServiceCategory;
import kz.open.sankaz.pojo.dto.ServiceCategoryDto;

public interface ServiceCategoryService extends CommonDictionaryService<ServiceCategory> {
    ServiceCategory addOneDto(ServiceCategoryDto dto);
    ServiceCategory updateOneDto(Long id, ServiceCategoryDto dto);
}
