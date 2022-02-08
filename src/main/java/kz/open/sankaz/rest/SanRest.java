package kz.open.sankaz.rest;

import kz.open.sankaz.mapper.SanMapper;
import kz.open.sankaz.pojo.dto.*;
import kz.open.sankaz.pojo.filter.SanForMainFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.ReviewService;
import kz.open.sankaz.service.RoomService;
import kz.open.sankaz.service.SanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public SanRest(SanService sanService) {
        this.sanService = sanService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestBody SanForMainFilter filter) {
        try{
            return ResponseModel.success(sanService.getAllForMain(filter));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{sanId}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "sanId") Long sanId) {
        try{
            return ResponseModel.success(sanService.getOneDto(sanId));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@RequestBody SanCreateDto sanDto) {
        try{
            return ResponseModel.success(sanMapper.sanToDto(sanService.addOneDto(sanDto)));
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
                                         @RequestBody SanCreateDto sanDto) {
        try{
            sanService.updateOneDto(sanId, sanDto);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/san-types/list")
    public ResponseEntity<?> addSanTypes(@PathVariable(name = "sanId") Long sanId,
                                           @RequestBody SanAddDeleteTypesDto dto) {
        try {
            return ResponseModel.success(sanService.addSanTypes(sanId, dto.getSanTypeIds()));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{sanId}/san-types/list")
    public ResponseEntity<?> deleteSanTypes(@PathVariable(name = "sanId") Long sanId,
                                            @RequestBody SanAddDeleteTypesDto dto) {
        try {
            sanService.deleteSanTypes(sanId, dto.getSanTypeIds());
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/tel-numbers/list")
    public ResponseEntity<?> addTelNumbers(@PathVariable(name = "sanId") Long sanId,
                                         @RequestBody SanAddDeleteNumbersDto dto) {
        try {
            return ResponseModel.success(sanService.addTelNumbers(sanId, dto.getTelNumbers()));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{sanId}/tel-numbers/list")
    public ResponseEntity<?> deleteTelNumbers(@PathVariable(name = "sanId") Long sanId,
                                            @RequestBody SanAddDeleteNumbersDto dto) {
        try {
            sanService.deleteTelNumbers(sanId, dto.getTelNumbers());
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/pics/list")
    public ResponseEntity<?> addPics(@PathVariable(name = "sanId") Long sanId,
                                           @RequestParam("pics") MultipartFile[] pics) {
        try {
            return ResponseModel.success(sanService.addPics(sanId, pics));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{sanId}/pics/list")
    public ResponseEntity<?> deletePic(@PathVariable(name = "sanId") Long sanId,
                                        @RequestBody SanDeletePicsDto dto) {
        try {
            sanService.deletePics(sanId, dto.getPicIds());
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/reviews")
    public ResponseEntity<?> addReview(@PathVariable(name = "sanId") Long sanId,
                                     @RequestBody ReviewCreateDto dto) {
        try {
            return ResponseModel.success(sanService.addReview(sanId, dto));
        } catch (Exception e) {
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
                                       @RequestBody RoomCreateDto dto) {
        try {
            return ResponseModel.success(sanService.addRoom(sanId, dto));
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
                                           @RequestBody SanDeletePicsDto dto) {
        try {
            sanService.deleteRoomPics(roomId, dto.getPicIds());
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
