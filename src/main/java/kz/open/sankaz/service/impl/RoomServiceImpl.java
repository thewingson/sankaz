package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.RoomClassDic;
import kz.open.sankaz.model.San;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.pojo.filter.RoomCreateFilter;
import kz.open.sankaz.repo.RoomRepo;
import kz.open.sankaz.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class RoomServiceImpl extends AbstractService<Room, RoomRepo> implements RoomService {

    @Autowired
    private RoomClassDicService roomClassDicService;

    @Autowired
    private SysFileService sysFileService;

    @Lazy
    @Autowired
    private AuthService authService;

    @Lazy
    @Autowired
    private SanService sanService;

    @Autowired
    private OrganizationService organizationService;


    public RoomServiceImpl(RoomRepo roomRepo) {
        super(roomRepo);
    }

    @Override
    public Room addOne(RoomCreateFilter filter) {
        Room room = new Room();
        room.setRoomClassDic(roomClassDicService.getOne(filter.getRoomClassDicId()));
        room.setRoomNumber(filter.getRoomNumber());
        room.setBedCount(filter.getBedCount());
        room.setRoomCount(filter.getRoomCount());
        room.setPrice(filter.getPrice());
        return addOne(room);
    }

    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Room addOne(RoomCreateFilter filter, MultipartFile[] pics) throws IOException {
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
        room.setPriceChild(filter.getPriceChild());
        return addPics(addOne(room), pics);
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
        room.setPriceChild(filter.getPriceChild());
        return editOneById(room);
    }

    private Room addPics(Room room, MultipartFile[] pics) throws IOException {

        for(MultipartFile pic : pics){
            if (!pic.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(APPLICATION_UPLOAD_PATH_IMAGE);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + pic.getOriginalFilename();
                String fileNameWithPath = APPLICATION_UPLOAD_PATH_IMAGE + "/" + resultFilename;

                pic.transferTo(new File(fileNameWithPath));

                SysFile file = new SysFile();
                file.setFileName(resultFilename);
                file.setExtension(pic.getContentType());
                file.setSize(pic.getSize());
                file = sysFileService.addOne(file);

                room.addPic(file);
            }
        }

        return room;
    }

    @Override
    public List<SysFile> addPics(Long roomId, MultipartFile[] pics) throws IOException {
        log.info(getServiceClass().getSimpleName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " Started");
        Room room = getOne(roomId);

        List<SysFile> savedPics = new ArrayList<>();
        for(MultipartFile pic : pics){
            if (!pic.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(APPLICATION_UPLOAD_PATH_IMAGE);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + pic.getOriginalFilename();
                String fileNameWithPath = APPLICATION_UPLOAD_PATH_IMAGE + "/" + resultFilename;

                pic.transferTo(new File(fileNameWithPath));

                SysFile file = new SysFile();
                file.setFileName(resultFilename);
                file.setExtension(pic.getContentType());
                file.setSize(pic.getSize());
                file = sysFileService.addOne(file);
                savedPics.add(file);

                room.addPic(file);
            }
        }

        return savedPics;
    }

    @Override
    public void deletePics(Long roomId, Long[] picIds) {
        log.info(getServiceClass().getSimpleName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " Started");
        Room room = getOne(roomId);

        List<SysFile> picsToDelete = sysFileService.getAllByIdIn(Arrays.asList(picIds));
        picsToDelete.stream().forEach(file -> {
            file.setDeletedDate(LocalDate.now());
            sysFileService.editOneById(file);
        });
        room.deletePics(picsToDelete);
        editOneById(room);
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
