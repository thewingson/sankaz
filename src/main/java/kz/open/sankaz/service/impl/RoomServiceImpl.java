package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.RoomDto;
import kz.open.sankaz.listener.event.CreateEvent;
import kz.open.sankaz.listener.event.DeleteEvent;
import kz.open.sankaz.listener.event.UpdateEvent;
import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.San;
import kz.open.sankaz.repo.RoomRepo;
import kz.open.sankaz.service.RoomService;
import kz.open.sankaz.service.SanService;
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
    public RoomDto getOneDto(Long id) {
        Room one = getOne(id);
        return roomMapper.roomToDto(one);
    }

    @Override
    public List<RoomDto> getAllDto() {
        return roomMapper.roomToDto(getAll());
    }

    @Override
    public List<RoomDto> getAllDto(Map<String, Object> params) {
        return roomMapper.roomToDto(getAll(params));
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
        if(roomDto.getName() != null){
            room.setName(roomDto.getName());
        }
        if(roomDto.getDescription() != null){
            room.setDescription(roomDto.getDescription());
        }
        if(roomDto.getSan() != null){
            San sanById = sanService
                    .getOne(roomDto.getSan().getId());
            room.setSan(sanById);
        }
        return editOneById(room);
    }

    @Override
    public Room updateOneDto(Map<String, Object> params, RoomDto roomDto) {
        // Backlog: потом, с помощью JOOQ
        return null;
    }

    @Override
    protected Class getCurrentClass() {
        return Room.class;
    }

    @Override
    protected ApplicationEvent getCreateEvent(Room room) {
        return new CreateEvent(room);
    }

    @Override
    protected ApplicationEvent getDeleteEvent(Room room) {
        return new DeleteEvent(room);
    }

    @Override
    protected ApplicationEvent getUpdateEvent(Room room) {
        return new UpdateEvent(room);
    }
}
