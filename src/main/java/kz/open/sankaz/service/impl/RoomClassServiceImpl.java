package kz.open.sankaz.service.impl;

import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.RoomClass;
import kz.open.sankaz.model.San;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.pojo.filter.RoomClassCreateFilter;
import kz.open.sankaz.repo.RoomClassRepo;
import kz.open.sankaz.service.RoomClassService;
import kz.open.sankaz.service.SanService;
import kz.open.sankaz.service.SysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class RoomClassServiceImpl extends AbstractService<RoomClass, RoomClassRepo> implements RoomClassService {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private SanService sanService;

    @Autowired
    private SysFileService sysFileService;


    public RoomClassServiceImpl(RoomClassRepo roomClassRepo) {
        super(roomClassRepo);
    }

    @Override
    protected Class getCurrentClass() {
        return Room.class;
    }

    @Override
    public RoomClass addOne(RoomClassCreateFilter filter) {
        log.info(getServiceClass().getSimpleName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " Started");
        San san = sanService.getOne(filter.getSanId());

        RoomClass roomClass = new RoomClass();
        roomClass.setSan(san);
        roomClass.setName(filter.getName());
        roomClass.setDescription(filter.getDescription());
        roomClass.setRoomCount(filter.getRoomCount());
        roomClass.setBedCount(filter.getBedCount());
        roomClass.setPrice(filter.getPrice());
        return addOne(roomClass);
    }

    @Override
    public RoomClass editOneById(Long classId, RoomClassCreateFilter filter) {
        log.info(getServiceClass().getSimpleName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " Started");
        RoomClass roomClass = getOne(classId);
        if(!roomClass.getSan().getId().equals(filter.getSanId())){
            roomClass.setSan(sanService.getOne(filter.getSanId()));
        }
        roomClass.setName(filter.getName());
        roomClass.setDescription(filter.getDescription());
        roomClass.setRoomCount(filter.getRoomCount());
        roomClass.setBedCount(filter.getBedCount());
        roomClass.setPrice(filter.getPrice());
        return editOneById(roomClass);
    }

    @Override
    public List<SysFile> addPics(Long classId, MultipartFile[] pics) throws IOException {
        log.info(getServiceClass().getSimpleName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " Started");
        RoomClass roomClass = getOne(classId);

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

                roomClass.addPic(file);
            }
        }

        return editOneById(roomClass).getPics();
    }

    @Override
    public void deletePics(Long classId, Long[] picIds) {
        log.info(getServiceClass().getSimpleName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " Started");
        RoomClass roomClass = getOne(classId);

        List<SysFile> picsToDelete = sysFileService.getAllByIdIn(Arrays.asList(picIds));
        picsToDelete.stream().forEach(file -> {
            file.setDeletedDate(LocalDate.now());
            sysFileService.editOneById(file);
        });
        roomClass.deletePics(picsToDelete);
        editOneById(roomClass);
    }

    protected Class getServiceClass(){
        return this.getClass();
    }
}
