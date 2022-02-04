package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.CompanyCategoryDto;
import kz.open.sankaz.mapper.CategoryMapper;
import kz.open.sankaz.model.CompanyCategory;
import kz.open.sankaz.repo.CompanyCategoryRepo;
import kz.open.sankaz.service.CompanyCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class CompanyCategoryServiceImpl extends AbstractDictionaryService<CompanyCategory, CompanyCategoryRepo> implements CompanyCategoryService {

    private final CompanyCategoryRepo companyCategoryRepo;

    @Autowired
    private CategoryMapper categoryMapper;


    public CompanyCategoryServiceImpl(CompanyCategoryRepo companyCategoryRepo) {
        super(companyCategoryRepo);
        this.companyCategoryRepo = companyCategoryRepo;
    }

    @Override
    protected Class getCurrentClass() {
        return CompanyCategory.class;
    }

    @Override
    public CompanyCategoryDto getOneDto(Long id) {
        return null;
    }

    @Override
    public List<CompanyCategoryDto> getAllDto() {
        return null;
    }

    @Override
    public List<CompanyCategoryDto> getAllDto(Map<String, Object> params) {
        return null;
    }

    @Override
    public CompanyCategory addOneDto(CompanyCategoryDto dto) {
        CompanyCategory companyCategory = categoryMapper.dtoToCompanyCategory(dto);
        return addOne(companyCategory);
    }

    @Override
    public CompanyCategory updateOneDto(Long id, CompanyCategoryDto dto) {
        CompanyCategory companyCategory = getOne(id);
        if(dto.getCode() != null && !dto.getCode().equals(companyCategory.getCode())){
            companyCategory.setCode(dto.getCode());
        }
        if(dto.getName() != null && !dto.getName().equals(companyCategory.getName())){
            companyCategory.setName(dto.getName());
        }
        if(dto.getDescription() != null && !dto.getDescription().equals(companyCategory.getDescription())){
            companyCategory.setDescription(dto.getDescription());
        }
        if(dto.getNameKz() != null && !dto.getNameKz().equals(companyCategory.getNameKz())){
            companyCategory.setNameKz(dto.getNameKz());
        }
        if(dto.getDescriptionKz() != null && !dto.getDescriptionKz().equals(companyCategory.getDescriptionKz())){
            companyCategory.setDescriptionKz(dto.getDescriptionKz());
        }
        return editOneById(companyCategory);
    }

    @Override
    public CompanyCategory updateOneDto(Map<String, Object> params, CompanyCategoryDto dto) {
        return null;
    }
}
