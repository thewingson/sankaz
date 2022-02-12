package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.mapper.CategoryMapper;
import kz.open.sankaz.model.RoomType;
import kz.open.sankaz.pojo.dto.RoomTypeDto;
import kz.open.sankaz.repo.dictionary.RoomTypeRepo;
import kz.open.sankaz.service.RoomTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class RoomTypeServiceImpl extends AbstractDictionaryService<RoomType, RoomTypeRepo> implements RoomTypeService {

    private final RoomTypeRepo roomTypeRepo;

    @Autowired
    private CategoryMapper categoryMapper;


    public RoomTypeServiceImpl(RoomTypeRepo roomTypeRepo) {
        super(roomTypeRepo);
        this.roomTypeRepo = roomTypeRepo;
    }

    @Override
    protected Class getCurrentClass() {
        return RoomType.class;
    }

    @Override
    public RoomType addOneDto(RoomTypeDto dto) {
        try{
            getOneByCode(dto.getCode());
            log.info(getServiceClass() + ".addOneDto()" + ". Code is busy");
            throw new RuntimeException("К сожалению кодовое название занято! Пожалуйста, введите другое название.");
        } catch (EntityNotFoundException e){
            log.info(getServiceClass() + ".addOneDto()" + ". Code is free");
        }
        RoomType roomType = categoryMapper.dtoToRoomType(dto);
        return addOne(roomType);
    }

    @Override
    public RoomType updateOneDto(Long id, RoomTypeDto dto) {
        RoomType roomType = getOne(id);
        try{
            getOneByCode(dto.getCode());
            log.info(getServiceClass() + ".updateOneDto()" + ". Code is busy");
            throw new RuntimeException("К сожалению кодовое название занято! Пожалуйста, введите другое название.");
        } catch (EntityNotFoundException e){
            log.info(getServiceClass() + ".updateOneDto()" + ". Code is free");
        }
        if(dto.getCode() != null && !dto.getCode().equals(roomType.getCode())){
            roomType.setCode(dto.getCode());
        }
        if(dto.getName() != null && !dto.getName().equals(roomType.getName())){
            roomType.setName(dto.getName());
        }
        if(dto.getDescription() != null && !dto.getDescription().equals(roomType.getDescription())){
            roomType.setDescription(dto.getDescription());
        }
        if(dto.getNameKz() != null && !dto.getNameKz().equals(roomType.getNameKz())){
            roomType.setNameKz(dto.getNameKz());
        }
        if(dto.getDescriptionKz() != null && !dto.getDescriptionKz().equals(roomType.getDescriptionKz())){
            roomType.setDescriptionKz(dto.getDescriptionKz());
        }
        return editOneById(roomType);
    }

    @Override
    protected Class getServiceClass(){
        return this.getClass();
    }
}
