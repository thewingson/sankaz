package kz.open.sankaz.rest.moder;

import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.pojo.filter.DeletePicsFilter;
import kz.open.sankaz.pojo.filter.RoomCreateFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_MODERATOR')")
@RestController
@RequestMapping("/moders/rooms")
public class ModerRoomRest {

    private final RoomService roomService;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    public ModerRoomRest(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getAllByClass(@PathVariable(name = "classId") Long classId) {
        try {
            return ResponseModel.success(roomMapper.roomToRoomCreateDto(roomService.getAllByClass(classId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getOne(@PathVariable("roomId") Long roomId) {
        try {
            return ResponseModel.success(roomMapper.roomToRoomCreateDto(roomService.getOne(roomId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@RequestParam String roomNumber,
                                    @RequestParam Long roomClassDicId,
                                    @RequestParam Integer roomCount,
                                    @RequestParam Integer bedCount,
                                    @RequestParam BigDecimal price,
                                    @RequestParam(value = "pics", required = false) MultipartFile[] pics) {
        try {
            RoomCreateFilter filter = new RoomCreateFilter(roomNumber, roomClassDicId, roomCount, bedCount, price);
            return ResponseModel.success(roomMapper.roomToRoomCreateDto(roomService.addOne(filter, pics)));
        } catch (Exception e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<?> editOneById(@PathVariable("roomId") Long roomId,
                                         @Valid @RequestBody RoomCreateFilter filter) {
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

    @PutMapping("/{roomId}/pics/list")
    public ResponseEntity<?> addRoomPics(@PathVariable(name = "roomId") Long roomId,
                                         @RequestParam("pics") MultipartFile[] pics) {
        try {
            return ResponseModel.success(roomService.addPics(roomId, pics));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{roomId}/pics/list")
    public ResponseEntity<?> deleteRoomPic(@PathVariable(name = "roomId") Long roomId,
                                           @Valid @RequestBody DeletePicsFilter filter) {
        try {
            roomService.deletePics(roomId, filter.getPicIds());
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}