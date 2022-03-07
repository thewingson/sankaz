package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.Organization;
import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.pojo.filter.RoomCreateFilter;
import kz.open.sankaz.repo.RoomRepo;
import kz.open.sankaz.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Override
    public Room addOne(RoomCreateFilter filter, MultipartFile[] pics) throws IOException {
        Room room = new Room();
        room.setRoomClassDic(roomClassDicService.getOne(filter.getRoomClassDicId()));
        room.setRoomNumber(filter.getRoomNumber());
        room.setBedCount(filter.getBedCount());
        room.setRoomCount(filter.getRoomCount());
        room.setPrice(filter.getPrice());
        return addPics(addOne(room), pics);
    }

    @Override
    public Room editOneById(Long roomId, RoomCreateFilter filter) {
        Room room = getOne(roomId);
        room.setRoomClassDic(roomClassDicService.getOne(filter.getRoomClassDicId()));
        room.setRoomNumber(filter.getRoomNumber());
        room.setBedCount(filter.getBedCount());
        room.setRoomCount(filter.getRoomCount());
        room.setPrice(filter.getPrice());
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

        return editOneById(room).getPics();
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
    public boolean checkIfOwnRoom(Long roomId) {
        String currentUsername = authService.getCurrentUsername();
        Organization currentUsersOrganization = organizationService.getOrganizationByTelNumber(currentUsername);
        Room roomById = getOne(roomId);
        if(!roomById.getRoomClassDic().getSan().getOrganization().getId().equals(currentUsersOrganization.getId())){
            throw new RuntimeException("Данная комната не прикреплен к вашему санаторию!");
        }
        return true;
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
