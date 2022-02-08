package kz.open.sankaz.service.impl;

import kz.open.sankaz.mapper.FileMapper;
import kz.open.sankaz.mapper.ReviewMapper;
import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.mapper.SanMapper;
import kz.open.sankaz.model.*;
import kz.open.sankaz.pojo.dto.*;
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
import java.util.*;
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

    @Autowired
    private RoomMapper roomMapper;

    @Value("${application.file.upload.path}")
    private String APPLICATION_UPLOAD_PATH;

    public SanServiceImpl(SanRepo sanRepo) {
        super(sanRepo);
        this.sanRepo = sanRepo;
    }

    @Override
    public SanDto getOneDto(Long id) {
        San one = getOne(id);
        return sanMapper.sanToDto(one);
    }

    @Override
    public List<SanDto> getAllDto() {
//        return sanMapper.sanToDto(getAll());
        return sanMapper.sanToDto(getAll());
    }

    @Override
    public List<SanDto> getAllDto(Map<String, Object> params) {
        return sanMapper.sanToDto(getAll(params));
    }

    @Override
    public San addOneDto(SanDto sanDto) {
        log.info("SERVICE -> SanServiceImpl.addOneDto()");
        San san = new San();
        san.setDescription(sanDto.getDescription());
        san.setName(sanDto.getName());

        if(sanDto.getSanTypes()!= null && !sanDto.getSanTypes().isEmpty()){
            List<SanType> sanTypesByCode = sanTypeService
                    .getAllByCodeIn(sanDto.getSanTypes().stream().map(SanTypeDto::getCode).collect(Collectors.toList()));
            san.setSanTypes(sanTypesByCode);
        }

        return addOne(san);
    }

    @Override
    public San updateOneDto(Long id, SanDto dto) {
        return null;
    }

    @Override
    public San updateOneDto(Long id, SanCreateDto dto) {
        log.info(getServiceClass() + ".updateOneDto() Started");
        log.info(getServiceClass() + ".updateOneDto() Checking San in DB");
        San san = getOne(id);

        log.info(getServiceClass() + ".addOneDto() Checking City in DB");
        City city = cityService.getOne(dto.getCityId());

        List<SanType> sanTypes = new ArrayList<>();
        log.info(getServiceClass() + ".updateOneDto() Checking San Types in DB");
        for (long type : dto.getSanTypes()) {
            sanTypes.add(sanTypeService.getOne(type));
        }
        san.setSanTypes(sanTypes);

        if(!city.equals(san.getCity())){
            san.setCity(city);
        }
        if(dto.getName() != null && !dto.getName().equals(san.getName())){
            san.setName(dto.getName());
        }
        if(dto.getDescription() != null && !dto.getDescription().equals(san.getDescription())){
            san.setDescription(dto.getDescription());
        }
        if(dto.getSiteLink() != null && !dto.getSiteLink().equals(san.getSiteLink())){
            san.setSiteLink(dto.getSiteLink());
        }
        if(dto.getInstagramLink() != null && !dto.getInstagramLink().equals(san.getInstagramLink())){
            san.setInstagramLink(dto.getInstagramLink());
        }

        if(dto.getTelNumbers() != null && dto.getTelNumbers().length > 0){
            List<String> dtoTelNumbers = Arrays.asList(dto.getTelNumbers());
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
    public SanDto addSanTypes(Long id, Long[] sanTypes) {
        log.info(getServiceClass() + ".addSanTypes() Started");
        log.info(getServiceClass() + ".addSanTypes() Checking San in DB");
        San san = getOne(id);

        log.info(getServiceClass() + ".addSanTypes() Checking San Types in DB");
        for (long typeId : sanTypes) {
            san.addSanType(sanTypeService.getOne(typeId));
        }

        log.info(getServiceClass() + ".addSanTypes() Finished");
        return sanMapper.sanToDto(editOneById(san));
    }

    @Override
    public void deleteSanTypes(Long id, Long[] sanTypes) {
        log.info(getServiceClass() + ".deleteSanTypes() Started");
        log.info(getServiceClass() + ".deleteSanTypes() Checking San in DB");
        San san = getOne(id);

        log.info(getServiceClass() + ".deleteSanTypes() Checking San Types in DB");
        for (long typeId : sanTypes) {
            san.deleteSanType(sanTypeService.getOne(typeId));
        }

        log.info(getServiceClass() + ".deleteSanTypes() Finished");
    }

    @Override
    public SanDto addTelNumbers(Long id, String[] telNumbers) {
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
        return sanMapper.sanToDto(editOneById(san));
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
    public List<FileUrlDto> addPics(Long id, MultipartFile[] pics) throws IOException {
        log.info(getServiceClass() + ".addPics() Started");
        log.info(getServiceClass() + ".addPics() Checking San in DB");
        San san = getOne(id);

        for(MultipartFile pic : pics){
            if (!pic.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(APPLICATION_UPLOAD_PATH);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + pic.getOriginalFilename();
                String fileNameWithPath = APPLICATION_UPLOAD_PATH + "/" + resultFilename;

                pic.transferTo(new File(fileNameWithPath));

                SysFile file = new SysFile();
                file.setFileName(resultFilename);
                file.setExtension(pic.getContentType());
                file.setSize(pic.getSize());
                file = sysFileService.addOne(file);

                san.addPic(file);
            }
        }

        log.info(getServiceClass() + ".addPics() Finished");
        return fileMapper.fileToFileUrlDto(editOneById(san).getPics());
    }

    @Override
    public void deletePics(Long sanId, Long[] pics) {
        log.info(getServiceClass() + ".deletePics() Started");
        log.info(getServiceClass() + ".deletePics() Checking San in DB");
        San san = getOne(sanId);

        List<SysFile> picsToDelete = sysFileService.getAllByIdIn(Arrays.asList(pics));
        san.deletePics(picsToDelete);
        editOneById(san);
        log.info(getServiceClass() + ".deletePics() Finished");
    }

    @Override
    public ReviewCreateDto addReview(Long sanId, ReviewCreateDto dto) {
        log.info(getServiceClass() + ".addReview() Started");
        log.info(getServiceClass() + ".addReview() Checking San in DB");
        Review review = reviewMapper.reviewCreateDtoToReview(dto);
        San san = getOne(sanId);
        SecUser user = userService.getUserByUsername(dto.getUsername());

        if(dto.getParentReviewId() != null){
            Review parentReview = reviewService.getOne(dto.getParentReviewId());
            review.setParentReview(parentReview);
        }
        review.setSan(san);
        review.setUser(user);

        reviewService.addOne(review);

        log.info(getServiceClass() + ".addReview() Finished");
        return reviewMapper.reviewToReviewCreateDto(review);
    }

    @Override
    public RoomCreateDto addRoom(Long sanId, RoomCreateDto dto) {
        log.info(getServiceClass() + ".addRoom() Started");
        log.info(getServiceClass() + ".addRoom() Checking San in DB");
        San san = getOne(sanId);

        Room room = new Room();
        room.setSan(san);
        room.setPrice(dto.getPrice());
        room.setName(dto.getName());
        room.setDescription(dto.getDescription());
        roomService.addOne(room);

        log.info(getServiceClass() + ".addRoom() Finished");
        return roomMapper.roomToRoomCreateDto(room);
    }

    @Override
    public List<FileUrlDto> addRoomPics(Long roomId, MultipartFile[] pics) throws IOException {
        log.info(getServiceClass() + ".addRoomPics() Started");
        log.info(getServiceClass() + ".addRoomPics() Checking San in DB");
        Room room = roomService.getOne(roomId);

        for(MultipartFile pic : pics){
            if (!pic.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(APPLICATION_UPLOAD_PATH);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + pic.getOriginalFilename();
                String fileNameWithPath = APPLICATION_UPLOAD_PATH + "/" + resultFilename;

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
        List<San> result = sanRepo.getAllBySanForMainFilter(filter.getCityId());

        return result.stream().map(san -> {
            SanForMainDto dto = new SanForMainDto();
            dto.setId(san.getId());
            dto.setName(san.getName());
            dto.setDescription(san.getDescription());
            if(san.getMainPicUrl() != null) dto.setPicUrl(APPLICATION_UPLOAD_PATH + san.getMainPicUrl());
            dto.setRating(san.getRating());
            dto.setReviewCount(san.getReviewCount());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public San updateOneDto(Map<String, Object> params, SanDto sanDto) {
        // Backlog: потом, с помощью JOOQ
        return null;
    }

    @Override
    protected Class getCurrentClass() {
        return San.class;
    }

    @Override
    public San addOneDto(SanCreateDto dto) {
        log.info(getServiceClass() + ".addOneDto() Started");

        log.info(getServiceClass() + ".addOneDto() Checking City in DB");
        City city = cityService.getOne(dto.getCityId());

        List<SanType> sanTypes = new ArrayList<>();
        log.info(getServiceClass() + ".addOneDto() Checking San Types in DB");
        for (long type : dto.getSanTypes()) {
            sanTypes.add(sanTypeService.getOne(type));
        }

        log.info(getServiceClass() + ".addOneDto() Creating San");
        San san = new San();
        san.setName(dto.getName());
        san.setDescription(dto.getDescription());
        san.setSiteLink(dto.getSiteLink());
        san.setInstagramLink(dto.getInstagramLink());
        san.setSanTypes(sanTypes);
        san.setCity(city);

        for (String telNumber : dto.getTelNumbers()) {
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
