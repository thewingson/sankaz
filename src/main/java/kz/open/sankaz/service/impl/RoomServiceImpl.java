package kz.open.sankaz.service.impl;

import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.San;
import kz.open.sankaz.pojo.dto.RoomDto;
import kz.open.sankaz.repo.RoomRepo;
import kz.open.sankaz.service.RoomService;
import kz.open.sankaz.service.SanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@Transactional
public class RoomServiceImpl extends AbstractService<Room, RoomRepo> implements RoomService {

    private final RoomRepo roomRepo;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private SanService sanService;


    public RoomServiceImpl(RoomRepo roomRepo) {
        super(roomRepo);
        this.roomRepo = roomRepo;
    }

    @Override
    public Room addOneDto(RoomDto roomDto) {
        log.info("SERVICE -> RoomServiceImpl.addOneDto()");
        Room room = new Room();
        room.setName(roomDto.getName());
        room.setDescription(roomDto.getDescription());
        room.setPrice(roomDto.getPrice());

        San sanById = sanService
                .getOne(roomDto.getSan().getId());
        room.setSan(sanById);

        return addOne(room);
    }

    @Override
    public Room updateOneDto(Long id, RoomDto roomDto) {
        log.info("SERVICE -> RoomServiceImpl.updateOneDto()");
        Room room = getOne(id);
        if(roomDto.getName() != null && !roomDto.getName().equals(room.getName())){
            room.setName(roomDto.getName());
        }
        if(roomDto.getDescription() != null && !roomDto.getDescription().equals(room.getDescription())){
            room.setDescription(roomDto.getDescription());
        }
        if(roomDto.getSan() != null && !roomDto.getSan().getId().equals(room.getSan().getId())){
            San sanById = sanService.getOne(roomDto.getSan().getId());
            room.setSan(sanById);
        }
        return editOneById(room);
    }

    @Override
    protected Class getCurrentClass() {
        return Room.class;
    }

}
