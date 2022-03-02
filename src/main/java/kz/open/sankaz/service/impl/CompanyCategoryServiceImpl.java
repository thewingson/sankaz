package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.CompanyCategory;
import kz.open.sankaz.pojo.filter.DictionaryLangFilter;
import kz.open.sankaz.repo.dictionary.CompanyCategoryRepo;
import kz.open.sankaz.service.CompanyCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompanyCategoryServiceImpl extends AbstractDictionaryService<CompanyCategory, CompanyCategoryRepo> implements CompanyCategoryService {

    private final CompanyCategoryRepo companyCategoryRepo;

    public CompanyCategoryServiceImpl(CompanyCategoryRepo companyCategoryRepo) {
        super(companyCategoryRepo);
        this.companyCategoryRepo = companyCategoryRepo;
    }

    @Override
    protected Class getCurrentClass() {
        return CompanyCategory.class;
    }

    @Override
    public CompanyCategory updateOne(Long id, DictionaryLangFilter filter) {
        CompanyCategory companyCategory = getOne(id);
        companyCategory.setCode(filter.getCode());
        companyCategory.setName(filter.getName());
        companyCategory.setDescription(filter.getDescription());
        companyCategory.setNameKz(filter.getNameKz());
        companyCategory.setDescriptionKz(filter.getDescriptionKz());
        return addOne(companyCategory);
    }

    @Override
    public CompanyCategory addOne(DictionaryLangFilter filter) {
        CompanyCategory companyCategory = new CompanyCategory();
        companyCategory.setCode(filter.getCode());
        companyCategory.setName(filter.getName());
        companyCategory.setDescription(filter.getDescription());
        companyCategory.setNameKz(filter.getNameKz());
        companyCategory.setDescriptionKz(filter.getDescriptionKz());
        return addOne(companyCategory);
    }

    @Override
    protected Class getServiceClass(){
        return this.getClass();
    }
}
