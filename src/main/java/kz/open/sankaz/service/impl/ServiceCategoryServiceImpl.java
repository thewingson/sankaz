package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.ServiceCategoryDto;
import kz.open.sankaz.mapper.CategoryMapper;
import kz.open.sankaz.model.ServiceCategory;
import kz.open.sankaz.repo.ServiceCategoryRepo;
import kz.open.sankaz.service.ServiceCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class ServiceCategoryServiceImpl extends AbstractDictionaryService<ServiceCategory, ServiceCategoryRepo> implements ServiceCategoryService {

    private final ServiceCategoryRepo serviceCategoryRepo;

    @Autowired
    private CategoryMapper categoryMapper;


    public ServiceCategoryServiceImpl(ServiceCategoryRepo serviceCategoryRepo) {
        super(serviceCategoryRepo);
        this.serviceCategoryRepo = serviceCategoryRepo;
    }

    @Override
    protected Class getCurrentClass() {
        return ServiceCategory.class;
    }

    @Override
    public ServiceCategoryDto getOneDto(Long id) {
        return null;
    }

    @Override
    public List<ServiceCategoryDto> getAllDto() {
        return null;
    }

    @Override
    public List<ServiceCategoryDto> getAllDto(Map<String, Object> params) {
        return null;
    }

    @Override
    public ServiceCategory addOneDto(ServiceCategoryDto dto) {
        ServiceCategory serviceCategory = categoryMapper.dtoToServiceCategory(dto);
        return addOne(serviceCategory);
    }

    @Override
    public ServiceCategory updateOneDto(Long id, ServiceCategoryDto dto) {
        ServiceCategory serviceCategory = getOne(id);
        if(dto.getCode() != null && !dto.getCode().equals(serviceCategory.getCode())){
            serviceCategory.setCode(dto.getCode());
        }
        if(dto.getName() != null && !dto.getName().equals(serviceCategory.getName())){
            serviceCategory.setName(dto.getName());
        }
        if(dto.getDescription() != null && !dto.getDescription().equals(serviceCategory.getDescription())){
            serviceCategory.setDescription(dto.getDescription());
        }
        if(dto.getNameKz() != null && !dto.getNameKz().equals(serviceCategory.getNameKz())){
            serviceCategory.setNameKz(dto.getNameKz());
        }
        if(dto.getDescriptionKz() != null && !dto.getDescriptionKz().equals(serviceCategory.getDescriptionKz())){
            serviceCategory.setDescriptionKz(dto.getDescriptionKz());
        }
        return editOneById(serviceCategory);
    }

    @Override
    public ServiceCategory updateOneDto(Map<String, Object> params, ServiceCategoryDto dto) {
        return null;
    }
}
