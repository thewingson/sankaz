package kz.open.sankaz.service.impl;

import kz.open.sankaz.image.SanaTourImage;
import kz.open.sankaz.model.*;
import kz.open.sankaz.pojo.filter.RoomCreateFilter;
import kz.open.sankaz.repo.RoomRepo;
import kz.open.sankaz.service.*;
import kz.open.sankaz.util.ReSizerImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

//Todo удалить

@Service
@Slf4j
@Transactional
public class RoomServiceImpl extends AbstractService<Room, RoomRepo> implements RoomService {

    @Autowired
    private RoomClassDicService roomClassDicService;

    @Lazy
    @Autowired
    private AuthService authService;

    @Lazy
    @Autowired
    private SanService sanService;

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private SanaTourImageService sanaTourImageService;
    @Autowired
    private ReSizerImageService reSizerImageService;

    public RoomServiceImpl(RoomRepo roomRepo) {
        super(roomRepo);
    }



    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Room addOne(RoomCreateFilter filter) {
        RoomClassDic classDic = roomClassDicService.getOne(filter.getRoomClassDicId());
        San san = sanService.getOne(filter.getSanId());
        List<Room> rooms = repo.findRoomByNameAndSan(san.getId(), filter.getRoomNumber().toLowerCase());
        if(!rooms.isEmpty()){
            throw new RuntimeException("Такой номер уже записан! Выберите другой номер");
        }
        Room room = new Room();
        room.setRoomClassDic(classDic);
        room.setSan(san);
        room.setRoomNumber(filter.getRoomNumber());
        room.setBedCount(filter.getBedCount());
        room.setRoomCount(filter.getRoomCount());
        room.setPrice(filter.getPrice());
        room.setPriceChild(filter.getChildPrice());
        room.setAdditionals(filter.getRoomAdditionalDto());
        Room result=addOne(room);
        List<SanaTourImage> sanaTourImages= new ArrayList<>();
        for (byte[] imageByte :filter.getImages()){
            SanaTourImage sanaTourImage = new SanaTourImage();
            sanaTourImage.setType("R");
            sanaTourImage.setBase64Original(Base64.getEncoder().encodeToString(imageByte));
            sanaTourImage.setBase64Scaled(reSizerImageService.reSize(imageByte,240,240));
            sanaTourImage.setRoomId(result);
            sanaTourImages.add(sanaTourImage);
        }
        sanaTourImageService.saveAll(sanaTourImages);
        return result;

    }

    @Override
    public Room editOneById(Long roomId, RoomCreateFilter filter) {
        Room room = new Room();
        List<Room> rooms = repo.findRoomByNameAndSan(filter.getSanId(), filter.getRoomNumber().toLowerCase());
        if(!rooms.isEmpty()){
            for (Room item : rooms) {
                if(item.getId().equals(roomId)){
                    room = item;
                    break;
                }
            }
            if(room.getRoomNumber() == null){
                throw new RuntimeException("Такой номер уже записан! Выберите другой номер");
            }
        }
        if(room.getRoomNumber() == null){
            room = getOne(roomId);
        }

        room.setRoomClassDic(roomClassDicService.getOne(filter.getRoomClassDicId()));
        room.setRoomNumber(filter.getRoomNumber());
        room.setBedCount(filter.getBedCount());
        room.setRoomCount(filter.getRoomCount());
        room.setPrice(filter.getPrice());
        room.setPriceChild(filter.getChildPrice());
        room.setEnable(filter.getIsEnable());
        return editOneById(room);
    }






    @Override
    public List<Room> getAllByDate(Long sanId, LocalDateTime startDate, LocalDateTime endDate) {
        return repo.getAllFreeForBookingByDateRange(sanId, startDate, endDate);
    }

    @Override
    public List<Room> getAllByClass(Long classId) {
        return repo.getAllByRoomClassDicId(classId);
    }

    @Override
    protected Class getCurrentClass() {
        return Room.class;
    }

    @Override
    protected Class getServiceClass(){
        return this.getClass();
    }

}
