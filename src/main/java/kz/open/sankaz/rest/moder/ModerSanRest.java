package kz.open.sankaz.rest.moder;

import kz.open.sankaz.mapper.ReviewMapper;
import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.mapper.SanMapper;
import kz.open.sankaz.pojo.filter.*;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.ReviewService;
import kz.open.sankaz.service.RoomService;
import kz.open.sankaz.service.SanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_MODERATOR')")
@RestController
@RequestMapping("/moder/sans")
public class ModerSanRest {

    private final SanService sanService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private SanMapper sanMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    public ModerSanRest(SanService sanService) {
        this.sanService = sanService;
    }

    @GetMapping
    public ResponseEntity<?> getAllOwn() {
        try{
            return ResponseModel.success(sanMapper.sanToSanForMainDto(sanService.getAllOwn()));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{sanId}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "sanId") Long sanId) {
        try{
            sanService.checkIfOwnSan(sanId);
            return ResponseModel.success(sanMapper.sanToSanByIdDto(sanService.getOne(sanId)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@Valid @RequestBody SanCreateFilter filter) {
        try{
            sanService.checkIfOwnOrg(filter.getOrgId());
            return ResponseModel.success(sanMapper.sanToDto(sanService.createSan(filter)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{sanId}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "sanId") Long sanId) {
        try{
            sanService.checkIfOwnSan(sanId);
            sanService.deleteOneById(sanId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "sanId") Long sanId,
                                         @Valid @RequestBody SanCreateFilter filter) {
        try{
            sanService.checkIfOwnSan(sanId);
            sanService.updateOneDto(sanId, filter);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/geo")
    public ResponseEntity<?> changeGeo(@PathVariable(name = "sanId") Long sanId,
                                       @Valid @RequestBody GeoFilter filter) {
        try{
            sanService.checkIfOwnSan(sanId);
            sanService.addGeo(sanId, filter.getLongitude(), filter.getLatitude());
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/san-types")
    public ResponseEntity<?> changeSanType(@PathVariable(name = "sanId") Long sanId,
                                           @Valid @RequestBody SanAddDeleteTypesFilter filter) {
        try {
            sanService.checkIfOwnSan(sanId);
            return ResponseModel.success(sanMapper.sanToDto(sanService.changeSanType(sanId, filter.getSanTypeId())));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/tel-numbers/list")
    public ResponseEntity<?> addTelNumbers(@PathVariable(name = "sanId") Long sanId,
                                           @Valid @RequestBody SanAddDeleteNumbersFilter filter) {
        try {
            sanService.checkIfOwnSan(sanId);
            return ResponseModel.success(sanMapper.sanToDto(sanService.addTelNumbers(sanId, filter.getTelNumbers())));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{sanId}/tel-numbers/list")
    public ResponseEntity<?> deleteTelNumbers(@PathVariable(name = "sanId") Long sanId,
                                              @Valid @RequestBody SanAddDeleteNumbersFilter filter) {
        try {
            sanService.checkIfOwnSan(sanId);
            sanService.deleteTelNumbers(sanId, filter.getTelNumbers());
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/pics/list")
    public ResponseEntity<?> addPics(@PathVariable(name = "sanId") Long sanId,
                                     @RequestParam("pics") MultipartFile[] pics) {
        try {
            sanService.checkIfOwnSan(sanId);
            return ResponseModel.success(sanMapper.fileToDto(sanService.addPics(sanId, pics)));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{sanId}/pics/list")
    public ResponseEntity<?> deletePics(@PathVariable(name = "sanId") Long sanId,
                                        @Valid @RequestBody DeletePicsFilter filter) {
        try {
            sanService.checkIfOwnSan(sanId);
            sanService.deletePics(sanId, filter.getPicIds());
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{sanId}/reviews")
    public ResponseEntity<?> getReviews(@PathVariable(name = "sanId") Long sanId) {
        try{
            sanService.checkIfOwnSan(sanId);
            return ResponseModel.success(reviewMapper.reviewToReviewBySanIdDto(reviewService.getAllBySanId(sanId)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{sanId}/reviews/filter")
    public ResponseEntity<?> getReviewsByFilter(@PathVariable(name = "sanId") Long sanId, @Valid @RequestBody ReviewBySanIdFilter filter) {
        try{
            sanService.checkIfOwnSan(sanId);
            return ResponseModel.success(reviewMapper.reviewToReviewBySanIdDto(reviewService.getAllByFilter(sanId, filter)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{sanId}/rooms")
    public ResponseEntity<?> getAllRooms(@PathVariable(name = "sanId") Long sanId,
                                     @PathVariable(name = "roomId") Long roomId) {
        try{
            sanService.checkIfOwnSan(sanId);
            return ResponseModel.success(roomMapper.roomToRoomCreateDto(roomService.getOne(roomId)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{sanId}/rooms/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable(name = "sanId") Long sanId,
                                     @PathVariable(name = "roomId") Long roomId) {
        try{
            roomService.checkIfOwnRoom(roomId);
            return ResponseModel.success(roomMapper.roomToRoomCreateDto(roomService.getOne(roomId)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/rooms")
    public ResponseEntity<?> addRoom(@PathVariable(name = "sanId") Long sanId,
                                     @Valid @RequestBody RoomCreateFilter filter) {
        try {
            sanService.checkIfOwnSan(sanId); // check also roomCLassDic
            return ResponseModel.success(roomMapper.roomToRoomCreateDto(roomService.addOne(filter)));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/rooms/{roomId}")
    public ResponseEntity<?> editRoom(@PathVariable(name = "sanId") Long sanId,
                                      @PathVariable("roomId") Long roomId,
                                      @Valid @RequestBody RoomCreateFilter filter) {
        try {
            roomService.checkIfOwnRoom(roomId);
            return ResponseModel.success(roomMapper.roomToRoomCreateDto(roomService.editOneById(roomId, filter)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{sanId}/rooms/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable(name = "sanId") Long sanId,
                                        @PathVariable(name = "roomId") Long roomId) {
        try {
            roomService.checkIfOwnRoom(roomId);
            roomService.deleteOneById(roomId);
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/rooms/{roomId}/pics/list")
    public ResponseEntity<?> addRoomPics(@PathVariable(name = "sanId") Long sanId,
                                         @PathVariable(name = "roomId") Long roomId,
                                         @RequestParam("pics") MultipartFile[] pics) {
        try {
            roomService.checkIfOwnRoom(roomId);
            return ResponseModel.success(roomService.addPics(roomId, pics));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{sanId}/rooms/{roomId}/pics/list")
    public ResponseEntity<?> deleteRoomPic(@PathVariable(name = "sanId") Long sanId,
                                           @PathVariable(name = "roomId") Long roomId,
                                           @Valid @RequestBody SanDeletePicsFilter filter) {
        try {
            roomService.checkIfOwnRoom(roomId);
            roomService.deletePics(roomId, filter.getPicIds());
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
