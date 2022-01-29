package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.CategoryDto;
import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.mapper.CategoryMapper;
import kz.open.sankaz.model.Category;
import kz.open.sankaz.repo.CategoryRepo;
import kz.open.sankaz.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class CategoryServiceImpl extends AbstractService<Category, CategoryRepo> implements CategoryService {

    private final CategoryRepo categoryRepo;

    @Autowired
    private CategoryMapper categoryMapper;


    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        super(categoryRepo);
        this.categoryRepo = categoryRepo;
    }

    @Override
    public List<Category> getAllByCodeIn(List<String> codes) {
        return categoryRepo.getAllByCodeIn(codes);
    }

    @Override
    public CategoryDto getOneDto(Long id) {
        Category one = getOne(id);
        return categoryMapper.categoryToDto(one);
    }

    @Override
    public Category getOneDto(String code) {
        Optional<Category> entityById = categoryRepo.getByCode(code);
        if(!entityById.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("CODE", code);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return entityById.get();
    }

    @Override
    public List<CategoryDto> getAllDto() {
        Map<String, Object> params = new HashMap<>();
        params.put("deleted", false);
        return categoryMapper.categoryToDto(getAll(params));
    }

    @Override
    public List<CategoryDto> getAllDto(Map<String, Object> params) {
        return categoryMapper.categoryToDto(getAll(params));
    }

    @Override
    public Category addOneDto(CategoryDto categoryDto) {
        log.info("SERVICE -> CategoryServiceImpl.addOneDto()");
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setCode(categoryDto.getCode());

        return addOne(category);
    }

    @Override
    public Category updateOneDto(Long id, CategoryDto categoryDto) {
        log.info("SERVICE -> CategoryServiceImpl.updateOneDto()");
        Category category = getOne(id);
        if(categoryDto.getCode() != null && !categoryDto.getCode().equals(category.getCode())){
            category.setCode(categoryDto.getCode());
        }
        if(categoryDto.getName() != null && !categoryDto.getName().equals(category.getName())){
            category.setName(categoryDto.getName());
        }
        if(categoryDto.getDescription() != null && !categoryDto.getDescription().equals(category.getDescription())){
            category.setDescription(categoryDto.getDescription());
        }
        return editOneById(category);
    }

    @Override
    public Category updateOneDto(Map<String, Object> params, CategoryDto categoryDto) {
        // Backlog: потом, с помощью JOOQ
        return null;
    }

    @Override
    protected Class getCurrentClass() {
        return Category.class;
    }
}
