package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.mapper.CategoryMapper;
import kz.open.sankaz.model.SanType;
import kz.open.sankaz.pojo.dto.SanTypeDto;
import kz.open.sankaz.repo.dictionary.SanTypeRepo;
import kz.open.sankaz.service.SanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SanTypeServiceImpl extends AbstractDictionaryService<SanType, SanTypeRepo> implements SanTypeService {

    private final SanTypeRepo sanTypeRepo;

    @Autowired
    private CategoryMapper categoryMapper;


    public SanTypeServiceImpl(SanTypeRepo sanTypeRepo) {
        super(sanTypeRepo);
        this.sanTypeRepo = sanTypeRepo;
    }

    @Override
    public List<SanType> getAllByCodeIn(List<String> codes) {
        return sanTypeRepo.getAllByCodeIn(codes);
    }

    @Override
    public SanType addOneDto(SanTypeDto sanTypeDto) {

        if(sanTypeDto.getCode() == null || sanTypeDto.getCode().isEmpty()){
            throw new RuntimeException("Кодовое название не может быть пустым! Пожалуйста, задайте его.");
        }
        if(sanTypeDto.getName() == null || sanTypeDto.getName().isEmpty()){
            throw new RuntimeException("Название не может быть пустым! Пожалуйста, задайте его.");
        }
        try{
            getOneByCode(sanTypeDto.getCode());
            throw new RuntimeException("К сожалению кодовое название занято! Пожалуйста, введите другое название.");
        } catch (EntityNotFoundException e){
        }
        SanType sanType = new SanType();
        sanType.setName(sanTypeDto.getName());
        sanType.setDescription(sanTypeDto.getDescription());
        sanType.setCode(sanTypeDto.getCode());

        return addOne(sanType);
    }

    @Override
    public SanType updateOneDto(Long id, SanTypeDto sanTypeDto) {
        try{
            getOneByCode(sanTypeDto.getCode());
            throw new RuntimeException("К сожалению кодовое название занято! Пожалуйста, введите другое название.");
        } catch (EntityNotFoundException e){
        }
        SanType sanType = getOne(id);
        if(sanTypeDto.getCode() != null && !sanTypeDto.getCode().equals(sanType.getCode())){
            sanType.setCode(sanTypeDto.getCode());
        }
        if(sanTypeDto.getName() != null && !sanTypeDto.getName().equals(sanType.getName())){
            sanType.setName(sanTypeDto.getName());
        }
        if(sanTypeDto.getDescription() != null && !sanTypeDto.getDescription().equals(sanType.getDescription())){
            sanType.setDescription(sanTypeDto.getDescription());
        }
        return editOneById(sanType);
    }

    @Override
    protected Class getCurrentClass() {
        return SanType.class;
    }

    @Override
    protected Class getServiceClass(){
        return this.getClass();
    }
}
