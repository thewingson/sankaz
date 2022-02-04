package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.RoomTypeDto;
import kz.open.sankaz.mapper.CategoryMapper;
import kz.open.sankaz.model.RoomType;
import kz.open.sankaz.repo.RoomTypeRepo;
import kz.open.sankaz.service.RoomTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
    public RoomTypeDto getOneDto(Long id) {
        return null;
    }

    @Override
    public List<RoomTypeDto> getAllDto() {
        return null;
    }

    @Override
    public List<RoomTypeDto> getAllDto(Map<String, Object> params) {
        return null;
    }

    @Override
    public RoomType addOneDto(RoomTypeDto dto) {
        RoomType roomType = categoryMapper.dtoToRoomType(dto);
        return addOne(roomType);
    }

    @Override
    public RoomType updateOneDto(Long id, RoomTypeDto dto) {
        RoomType roomType = getOne(id);
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
    public RoomType updateOneDto(Map<String, Object> params, RoomTypeDto dto) {
        return null;
    }
}
