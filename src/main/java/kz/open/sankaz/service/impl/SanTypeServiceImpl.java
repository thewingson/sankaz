package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.SanTypeDto;
import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.mapper.CategoryMapper;
import kz.open.sankaz.model.SanType;
import kz.open.sankaz.repo.SanTypeRepo;
import kz.open.sankaz.service.SanTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
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
    public SanTypeDto getOneDto(Long id) {
        SanType one = getOne(id);
        return categoryMapper.sanTypeToDto(one);
    }

    @Override
    public List<SanTypeDto> getAllDto() {
        Map<String, Object> params = new HashMap<>();
        params.put("deleted", false);
        return categoryMapper.sanTypeToDto(getAll(params));
    }

    @Override
    public List<SanTypeDto> getAllDto(Map<String, Object> params) {
        return categoryMapper.sanTypeToDto(getAll(params));
    }

    @Override
    public SanType addOneDto(SanTypeDto sanTypeDto) {
        log.info(getServiceClass() + ".addOneDto()");

        if(sanTypeDto.getCode() == null || sanTypeDto.getCode().isEmpty()){
            throw new RuntimeException("Кодовое название не может быть пустым! Пожалуйста, задайте его.");
        }
        if(sanTypeDto.getName() == null || sanTypeDto.getName().isEmpty()){
            throw new RuntimeException("Название не может быть пустым! Пожалуйста, задайте его.");
        }
        try{
            getOneByCode(sanTypeDto.getCode());
            log.info(getServiceClass() + ".addOneDto()" + ". Code is busy");
            throw new RuntimeException("К сожалению кодовое название занято! Пожалуйста, введите другое название.");
        } catch (EntityNotFoundException e){
            log.info(getServiceClass() + ".addOneDto()" + ". Code is free");
        }
        SanType sanType = new SanType();
        sanType.setName(sanTypeDto.getName());
        sanType.setDescription(sanTypeDto.getDescription());
        sanType.setCode(sanTypeDto.getCode());

        return addOne(sanType);
    }

    @Override
    public SanType updateOneDto(Long id, SanTypeDto sanTypeDto) {
        log.info(getServiceClass() + ".updateOneDto()");
        try{
            getOneByCode(sanTypeDto.getCode());
            log.info(getServiceClass() + ".updateOneDto()" + ". Code is busy");
            throw new RuntimeException("К сожалению кодовое название занято! Пожалуйста, введите другое название.");
        } catch (EntityNotFoundException e){
            log.info(getServiceClass() + ".updateOneDto()" + ". Code is free");
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
    public SanType updateOneDto(Map<String, Object> params, SanTypeDto sanTypeDto) {
        // Backlog: потом, с помощью JOOQ
        return null;
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
