package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.CategoryDto;
import kz.open.sankaz.listener.event.CreateEvent;
import kz.open.sankaz.listener.event.DeleteEvent;
import kz.open.sankaz.listener.event.UpdateEvent;
import kz.open.sankaz.mapper.CategoryMapper;
import kz.open.sankaz.model.Category;
import kz.open.sankaz.repo.CategoryRepo;
import kz.open.sankaz.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

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
    public List<CategoryDto> getAllDto() {
        return categoryMapper.categoryToDto(getAll());
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
        if(categoryDto.getCode() != null){
            category.setCode(categoryDto.getCode());
        }
        if(categoryDto.getName() != null){
            category.setName(categoryDto.getName());
        }
        if(categoryDto.getDescription() != null){
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

    @Override
    protected ApplicationEvent getCreateEvent(Category category) {
        return new CreateEvent(category);
    }

    @Override
    protected ApplicationEvent getDeleteEvent(Category category) {
        return new DeleteEvent(category);
    }

    @Override
    protected ApplicationEvent getUpdateEvent(Category category) {
        return new UpdateEvent(category);
    }
}
