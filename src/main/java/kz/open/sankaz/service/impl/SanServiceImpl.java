package kz.open.sankaz.service.impl;

import kz.open.sankaz.mapper.FileMapper;
import kz.open.sankaz.mapper.ReviewMapper;
import kz.open.sankaz.mapper.SanMapper;
import kz.open.sankaz.model.*;
import kz.open.sankaz.pojo.dto.FileUrlDto;
import kz.open.sankaz.pojo.dto.SanForMainDto;
import kz.open.sankaz.pojo.filter.ReviewCreateFilter;
import kz.open.sankaz.pojo.filter.RoomCreateFilter;
import kz.open.sankaz.pojo.filter.SanCreateFilter;
import kz.open.sankaz.pojo.filter.SanForMainFilter;
import kz.open.sankaz.repo.SanRepo;
import kz.open.sankaz.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class SanServiceImpl extends AbstractService<San, SanRepo> implements SanService {

    private final SanRepo sanRepo;

    @Lazy
    @Autowired
    private SanTypeService sanTypeService;

    @Lazy
    @Autowired
    private UserService userService;

    @Autowired
    private TelNumberService telNumberService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private SysFileService sysFileService;

    @Lazy
    @Autowired
    private RoomService roomService;

    @Autowired
    private CityService cityService;

    @Autowired
    private SanMapper sanMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private FileMapper fileMapper;

    @Value("${application.file.upload.path.image}")
    private String APPLICATION_UPLOAD_PATH_IMAGE;

    @Value("${application.file.download.path.image}")
    private String APPLICATION_DOWNLOAD_PATH_IMAGE;

    public SanServiceImpl(SanRepo sanRepo) {
        super(sanRepo);
        this.sanRepo = sanRepo;
    }


    @Override
    public San updateOneDto(Long id, SanCreateFilter filter) {
        log.info(getServiceClass() + ".updateOneDto() Started");
        log.info(getServiceClass() + ".updateOneDto() Checking San in DB");
        San san = getOne(id);

        log.info(getServiceClass() + ".addOneDto() Checking City in DB");
        City city = cityService.getOne(filter.getCityId());

        List<SanType> sanTypes = new ArrayList<>();
        log.info(getServiceClass() + ".updateOneDto() Checking San Types in DB");
        if(filter.getSanTypeId()!= null){
            SanType sanTypeByCode = sanTypeService.getOne(filter.getSanTypeId());
            san.setSanType(sanTypeByCode);
        }

            san.setCity(city);
            san.setName(filter.getName());
            san.setDescription(filter.getDescription());
        if(filter.getSiteLink() != null){
            san.setSiteLink(filter.getSiteLink());
        }
        if(filter.getInstagramLink() != null){
            san.setInstagramLink(filter.getInstagramLink());
        }
        if(filter.getLongitude() != null){
            san.setLongitude(filter.getLongitude());
        }
        if(filter.getLatitude() != null){
            san.setLatitude(filter.getLatitude());
        }

        if(filter.getTelNumbers() != null && filter.getTelNumbers().length > 0){
            List<String> dtoTelNumbers = Arrays.asList(filter.getTelNumbers());
            List<TelNumber> toDelete = new ArrayList<>();
            san.getTelNumbers().stream().forEach(telNumber -> {
                if(!dtoTelNumbers.contains(telNumber.getValue())){
                    toDelete.add(telNumber);
                }
            });

            List<String> sanTelNumberValues = san.getTelNumbers().stream().map(TelNumber::getValue).collect(Collectors.toList());
            List<TelNumber> toCreate = new ArrayList<>();
            dtoTelNumbers.stream().forEach(dtoTelNumber -> {
                if(!sanTelNumberValues.contains(dtoTelNumber)){
                    TelNumber tel = new TelNumber();
                    tel.setValue(dtoTelNumber);
                    telNumberService.addOne(tel);
                    toCreate.add(tel);
                }
            });

            san.deleteTelNumbers(toDelete);
            san.addTelNumbers(toCreate);
        } else{
            san.deleteTelNumbers(san.getTelNumbers());
        }

        log.info(getServiceClass() + ".updateOneDto() Finished");
        return editOneById(san);
    }

    @Override
    public San changeSanType(Long id, Long sanTypeId) {
        log.info(getServiceClass() + ".changeSanType() Started");
        log.info(getServiceClass() + ".changeSanType() Checking San in DB");
        San san = getOne(id);

        log.info(getServiceClass() + ".changeSanType() Checking San Types in DB");
        san.setSanType(sanTypeService.getOne(sanTypeId));

        log.info(getServiceClass() + ".changeSanType() Finished");
        return san;
    }

    @Override
    public San addTelNumbers(Long id, String[] telNumbers) {
        log.info(getServiceClass() + ".addTelNumbers() Started");
        log.info(getServiceClass() + ".addTelNumbers() Checking San in DB");
        San san = getOne(id);

        if(telNumbers != null && telNumbers.length > 0){
            List<String> dtoTelNumbers = Arrays.asList(telNumbers);

            List<String> sanTelNumberValues = san.getTelNumbers().stream().map(TelNumber::getValue).collect(Collectors.toList());
            List<TelNumber> toCreate = new ArrayList<>();
            dtoTelNumbers.stream().forEach(dtoTelNumber -> {
                if(!sanTelNumberValues.contains(dtoTelNumber)){
                    TelNumber tel = new TelNumber();
                    tel.setValue(dtoTelNumber);
                    telNumberService.addOne(tel);
                    toCreate.add(tel);
                }
            });
            san.addTelNumbers(toCreate);
        }

        log.info(getServiceClass() + ".addTelNumbers() Finished");
        return san;
    }

    @Override
    public void deleteTelNumbers(Long id, String[] telNumbers) {
        log.info(getServiceClass() + ".addTelNumbers() Started");
        log.info(getServiceClass() + ".addTelNumbers() Checking San in DB");
        San san = getOne(id);

        if(telNumbers != null && telNumbers.length > 0){
            List<String> numbersToDelete = Arrays.asList(telNumbers);

            List<TelNumber> toDelete = new ArrayList<>();
            san.getTelNumbers().stream().forEach(telNumber -> {
                if(numbersToDelete.contains(telNumber.getValue())){
                    toDelete.add(telNumber);
                }
            });
            san.deleteTelNumbers(toDelete);
        }

        log.info(getServiceClass() + ".addTelNumbers() Finished");
    }

    @Override
    public FileUrlDto changePic(Long id, MultipartFile pic) throws IOException {
        log.info(getServiceClass() + ".addPics() Started");
        log.info(getServiceClass() + ".addPics() Checking San in DB");
        San san = getOne(id);

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

            san.setPic(file);
        }

        editOneById(san);
        log.info(getServiceClass() + ".addPics() Finished");
        return fileMapper.fileToFileUrlDto(san.getPic());
    }

    @Override
    public Review addReview(Long sanId, ReviewCreateFilter filter) {
        log.info(getServiceClass() + ".addReview() Started");
        log.info(getServiceClass() + ".addReview() Checking San in DB");
        Review review = reviewMapper.reviewCreateFilterToReview(filter);
        San san = getOne(sanId);
        SecUser user = userService.getUserByUsername(filter.getUsername());

        if(filter.getParentReviewId() != null){
            Review parentReview = reviewService.getOne(filter.getParentReviewId());
            review.setParentReview(parentReview);
        }
        review.setSan(san);
        review.setUser(user);

        reviewService.addOne(review);

        log.info(getServiceClass() + ".addReview() Finished");
        return review;
    }

    @Override
    public Room addRoom(Long sanId, RoomCreateFilter filter) {
        log.info(getServiceClass() + ".addRoom() Started");
        log.info(getServiceClass() + ".addRoom() Checking San in DB");
        San san = getOne(sanId);

        Room room = new Room();
        room.setSan(san);
        room.setPrice(filter.getPrice());
        room.setName(filter.getName());
        room.setDescription(filter.getDescription());
        roomService.addOne(room);

        log.info(getServiceClass() + ".addRoom() Finished");
        return room;
    }

    @Override
    public List<FileUrlDto> addRoomPics(Long roomId, MultipartFile[] pics) throws IOException {
        log.info(getServiceClass() + ".addRoomPics() Started");
        log.info(getServiceClass() + ".addRoomPics() Checking San in DB");
        Room room = roomService.getOne(roomId);

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

        log.info(getServiceClass() + ".addRoomPics() Finished");
        return fileMapper.fileToFileUrlDto(roomService.editOneById(room).getPics());
    }

    @Override
    public void deleteRoomPics(Long roomId, Long[] pics) {
        log.info(getServiceClass() + ".deleteRoomPics() Started");
        log.info(getServiceClass() + ".deleteRoomPics() Checking San in DB");
        Room room = roomService.getOne(roomId);

        List<SysFile> picsToDelete = sysFileService.getAllByIdIn(Arrays.asList(pics));
        room.deletePics(picsToDelete);
        roomService.editOneById(room);
        log.info(getServiceClass() + ".deleteRoomPics() Finished");
    }

    @Override
    public List<SanForMainDto> getAllForMain(SanForMainFilter filter) {
//        List<San> result = sanRepo.getAllBySanForMainFilter(filter.getCityId(), filter.getStartDate(), filter.getEndDate());
        List<San> result = sanRepo.getAllBySanForMainFilter(filter.getCityId()); // TODO: add other filters after Bokking creation

        return result.stream().map(san -> {
            SanForMainDto dto = new SanForMainDto();
            dto.setId(san.getId());
            dto.setName(san.getName());
            dto.setDescription(san.getDescription());
            if(san.getMainPicUrl() != null) dto.setPicUrl(APPLICATION_DOWNLOAD_PATH_IMAGE + san.getMainPicUrl());
            dto.setRating(san.getRating());
            dto.setReviewCount(san.getReviewCount());
            dto.setLongitude(san.getLongitude());
            dto.setLatitude(san.getLatitude());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void addGeo(Long sanId, Double longitude, Double latitude) {
        San san = getOne(sanId);
        san.setLatitude(latitude);
        san.setLongitude(longitude);
        editOneById(san);
    }

    @Override
    protected Class getCurrentClass() {
        return San.class;
    }

    @Override
    public San createSan(SanCreateFilter filter) {
        log.info(getServiceClass() + ".addOneDto() Started");

        log.info(getServiceClass() + ".addOneDto() Checking City in DB");
        City city = cityService.getOne(filter.getCityId());

        log.info(getServiceClass() + ".addOneDto() Checking San Types in DB");
        SanType sanType = sanTypeService.getOne(filter.getSanTypeId());

        log.info(getServiceClass() + ".addOneDto() Creating San");
        San san = new San();
        san.setName(filter.getName());
        san.setDescription(filter.getDescription());
        san.setCity(city);
        san.setSanType(sanType);

        if(filter.getSiteLink() != null){
            san.setSiteLink(filter.getSiteLink());
        }
        if(filter.getInstagramLink() != null){
            san.setInstagramLink(filter.getInstagramLink());
        }
        if(filter.getLongitude() != null){
            san.setLongitude(filter.getLongitude());
        }
        if(filter.getLatitude() != null){
            san.setLatitude(filter.getLatitude());
        }

        for (String telNumber : filter.getTelNumbers()) {
            TelNumber number = new TelNumber();
            number.setValue(telNumber);
            telNumberService.addOne(number);
            san.addTelNumber(number);
        }

        log.info(getServiceClass() + ".addOneDto() Finished");
        return addOne(san);
    }

    @Override
    protected Class getServiceClass(){
        return this.getClass();
    }
}
