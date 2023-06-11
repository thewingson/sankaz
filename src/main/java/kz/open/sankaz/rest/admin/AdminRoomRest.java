package kz.open.sankaz.rest.admin;

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

import static org.springframework.http.HttpStatus.BAD_REQUEST;

//@PreAuthorize("hasRole('ROLE_ADMIN')") //TODO
@RestController
@RequestMapping("/admin/rooms")
public class AdminRoomRest {

    private final RoomService roomService;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    public AdminRoomRest(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseModel.success(roomMapper.roomToRoomCreateDto(roomService.getAll()));
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
    public ResponseEntity<?> addOne(@Valid @RequestBody RoomCreateFilter filter) {
        try {
            return ResponseModel.success(roomMapper.roomToRoomCreateDto(roomService.addOne(filter)));
        } catch (RuntimeException e) {
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



}
