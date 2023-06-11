package kz.open.sankaz.rest.moder;

import kz.open.sankaz.image.SanaTourImage;
import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.San;
import kz.open.sankaz.pojo.filter.SanaTourImageDTO;
import kz.open.sankaz.pojo.filter.RoomCreateFilter;
import kz.open.sankaz.repo.RoomRepo;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.RoomAdditionalService;
import kz.open.sankaz.service.RoomService;
import kz.open.sankaz.service.SanaTourImageService;
import kz.open.sankaz.util.ReSizerImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_MODERATOR')")
@RestController
@RequestMapping("/moders/rooms")
public class ModerRoomRest {

    private final RoomService roomService;
    @Autowired
    RoomAdditionalService roomAdditionalService;
    @Autowired
    SanaTourImageService sanaTourImageService;
    @Autowired
    ReSizerImageService reSizerImageService;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    public ModerRoomRest(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/san/{sanId}/class/{classId}")
    public ResponseEntity<?> getAllByClassAndSan(@PathVariable(name = "sanId") Long sanId,
                                           @PathVariable(name = "classId") Long classId) {
        try {
            return ResponseModel.success(roomMapper.roomToRoomCreateDto(roomRepo.getAllByRoomClassDicIdAndSanId(classId, sanId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getOne(@PathVariable("roomId") Long roomId) {
        try {
            Room asd=roomService.getOne(roomId);
            return ResponseModel.success(roomMapper.roomToRoomCreateDto(asd));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@RequestBody RoomCreateFilter roomCreateFilter) {
        try {
            roomCreateFilter.setIsEnable(Boolean.TRUE);
            return ResponseModel.success(roomMapper.roomToRoomCreateDto(roomService.addOne(roomCreateFilter)));
        } catch (Exception e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<?> editOneById(@PathVariable("roomId") Long roomId,@Valid @RequestBody RoomCreateFilter filter) {
        try {
            return ResponseModel.success(roomMapper.roomToRoomCreateDto(roomService.editOneById(roomId, filter)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "roomId") Long roomId) {
        try{
            roomService.deleteOneById(roomId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @GetMapping
    @RequestMapping("/room-additionals")
    public ResponseEntity<?> getRoomAdditionals(){
        try {
            return ResponseModel.success(roomAdditionalService.getRoomAdditionals());
        }catch (Exception e){
            return ResponseModel.error(BAD_REQUEST,e.getMessage());
        }
    }

    //При редактирование Номера, фото здесь удаляет
    @DeleteMapping
    @RequestMapping("/delete/image/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable(name="id") Long id){
        try {
            sanaTourImageService.deleteOneById(id);
            return  ResponseModel.successPure();
        }catch (Exception e){
            return ResponseModel.error(BAD_REQUEST,e.getMessage());
        }
    }
    //При редактирование Номера, фото здесь добавляет
    @PostMapping
    @RequestMapping("/add/images")
    public ResponseEntity<?> addImagesToRoom(@RequestBody() SanaTourImageDTO dto){
        try {
        List<SanaTourImage> sanaTourImages = new ArrayList<>();
            dto.getImages().forEach(imageByte->{
            SanaTourImage sanaTourImage = new SanaTourImage();
            sanaTourImage.setType(dto.getType());
            if (!Objects.isNull(dto.getRoomId())){
                Room room =new Room();
                room.setId(dto.getRoomId());
                sanaTourImage.setRoomId(room);
            }
            if (!Objects.isNull(dto.getSanId())){
                San san = new San();
                san.setId(dto.getSanId());
                sanaTourImage.setSanId(san);
            }

            sanaTourImage.setBase64Original(Base64.getEncoder().encodeToString(imageByte));
            sanaTourImage.setBase64Scaled(reSizerImageService.reSize(imageByte,240,240));

            sanaTourImages.add(sanaTourImage);
        });
        sanaTourImageService.saveAll(sanaTourImages);
       return  ResponseModel.successPure();
        }catch (Exception e){
            return ResponseModel.error(BAD_REQUEST,e.getMessage());
        }
    }

}
