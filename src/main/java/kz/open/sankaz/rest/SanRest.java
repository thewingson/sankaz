package kz.open.sankaz.rest;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/sans")
public class SanRest {

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
    public SanRest(SanService sanService) {
        this.sanService = sanService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @RequestBody SanForMainFilter filter) {
        try{
            return ResponseModel.success(sanService.getAllForMain(filter));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{sanId}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "sanId") Long sanId) {
        try{
            return ResponseModel.success(sanMapper.sanToSanByIdDto(sanService.getOne(sanId)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@Valid @RequestBody SanCreateFilter filter) {
        try{
            return ResponseModel.success(sanMapper.sanToDto(sanService.createSan(filter)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{sanId}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "sanId") Long sanId) {
        try{
            sanService.deleteOneByIdSoft(sanId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "sanId") Long sanId,
                                         @Valid @RequestBody SanCreateFilter filter) {
        try{
            sanService.updateOneDto(sanId, filter);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/san-types")
    public ResponseEntity<?> changeSanType(@PathVariable(name = "sanId") Long sanId,
                                         @Valid @RequestBody SanAddDeleteTypesFilter filter) {
        try {
            return ResponseModel.success(sanMapper.sanToDto(sanService.changeSanType(sanId, filter.getSanTypeId())));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/tel-numbers/list")
    public ResponseEntity<?> addTelNumbers(@PathVariable(name = "sanId") Long sanId,
                                           @Valid @RequestBody SanAddDeleteNumbersFilter filter) {
        try {
            return ResponseModel.success(sanMapper.sanToDto(sanService.addTelNumbers(sanId, filter.getTelNumbers())));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{sanId}/tel-numbers/list")
    public ResponseEntity<?> deleteTelNumbers(@PathVariable(name = "sanId") Long sanId,
                                              @Valid @RequestBody SanAddDeleteNumbersFilter filter) {
        try {
            sanService.deleteTelNumbers(sanId, filter.getTelNumbers());
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/pics")
    public ResponseEntity<?> changePic(@PathVariable(name = "sanId") Long sanId,
                                           @RequestParam("pic") MultipartFile pic) {
        try {
            return ResponseModel.success(sanService.changePic(sanId, pic));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/reviews")
    public ResponseEntity<?> addReview(@PathVariable(name = "sanId") Long sanId,
                                       @Valid @RequestBody ReviewCreateFilter filter) {
        try {
            return ResponseModel.success(reviewMapper.reviewToReviewCreateDto(sanService.addReview(sanId, filter)));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{sanId}/reviews")
    public ResponseEntity<?> getReviews(@PathVariable(name = "sanId") Long sanId) {
        try{
            return ResponseModel.success(reviewMapper.reviewToReviewBySanIdDto(reviewService.getAllBySanId(sanId)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{sanId}/reviews/filter")
    public ResponseEntity<?> getReviewsByFilter(@PathVariable(name = "sanId") Long sanId, @Valid @RequestBody ReviewBySanIdFilter filter) {
        try{
            return ResponseModel.success(reviewMapper.reviewToReviewBySanIdDto(reviewService.getAllByFilter(sanId, filter)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{sanId}/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable(name = "sanId") Long sanId,
                                       @PathVariable(name = "reviewId") Long reviewId) {
        try {
            reviewService.deleteOneByIdSoft(reviewId);
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/rooms")
    public ResponseEntity<?> addRoom(@PathVariable(name = "sanId") Long sanId,
                                     @Valid @RequestBody RoomCreateFilter filter) {
        try {
            return ResponseModel.success(roomMapper.roomToRoomCreateDto(sanService.addRoom(sanId, filter)));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{sanId}/rooms/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable(name = "sanId") Long sanId,
                                        @PathVariable(name = "roomId") Long roomId) {
        try {
            roomService.deleteOneByIdSoft(roomId);
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
            return ResponseModel.success(sanService.addRoomPics(roomId, pics));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{sanId}/rooms/{roomId}/pics/list")
    public ResponseEntity<?> deleteRoomPic(@PathVariable(name = "sanId") Long sanId,
                                           @PathVariable(name = "roomId") Long roomId,
                                           @Valid @RequestBody SanDeletePicsFilter filter) {
        try {
            sanService.deleteRoomPics(roomId, filter.getPicIds());
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{sanId}/rooms/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable(name = "sanId") Long sanId,
                                     @PathVariable(name = "roomId") Long roomId) {
        try{
            return ResponseModel.success(roomMapper.roomToRoomByIdDto(roomService.getOne(roomId)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
