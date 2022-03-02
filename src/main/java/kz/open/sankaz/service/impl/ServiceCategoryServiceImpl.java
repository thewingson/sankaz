package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.ServiceCategory;
import kz.open.sankaz.pojo.filter.DictionaryLangFilter;
import kz.open.sankaz.repo.dictionary.ServiceCategoryRepo;
import kz.open.sankaz.service.ServiceCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServiceCategoryServiceImpl extends AbstractDictionaryService<ServiceCategory, ServiceCategoryRepo> implements ServiceCategoryService {

    private final ServiceCategoryRepo serviceCategoryRepo;

    public ServiceCategoryServiceImpl(ServiceCategoryRepo serviceCategoryRepo) {
        super(serviceCategoryRepo);
        this.serviceCategoryRepo = serviceCategoryRepo;
    }

    @Override
    protected Class getCurrentClass() {
        return ServiceCategory.class;
    }

    @Override
    public ServiceCategory addOne(DictionaryLangFilter filter) {
        ServiceCategory serviceCategory = new ServiceCategory();
        serviceCategory.setCode(filter.getCode());
        serviceCategory.setName(filter.getName());
        serviceCategory.setDescription(filter.getDescription());
        serviceCategory.setNameKz(filter.getNameKz());
        serviceCategory.setDescriptionKz(filter.getDescriptionKz());
        return addOne(serviceCategory);
    }

    @Override
    public ServiceCategory updateOne(Long id, DictionaryLangFilter filter) {
        ServiceCategory serviceCategory = getOne(id);
        serviceCategory.setCode(filter.getCode());
        serviceCategory.setName(filter.getName());
        serviceCategory.setDescription(filter.getDescription());
        serviceCategory.setNameKz(filter.getNameKz());
        serviceCategory.setDescriptionKz(filter.getDescriptionKz());
        return addOne(serviceCategory);
    }
}
