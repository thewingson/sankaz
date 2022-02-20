package kz.open.sankaz.rest.admin;

import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.pojo.filter.RoomClassDicFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.RoomClassDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/room-class-dics")
public class AdminRoomClassDicRest {

    private final RoomClassDicService roomClassDicService;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    public AdminRoomClassDicRest(RoomClassDicService roomClassDicService) {
        this.roomClassDicService = roomClassDicService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseModel.success(roomMapper.roomClassDicToDto(roomClassDicService.getAll()));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{dicId}")
    public ResponseEntity<?> getOne(@PathVariable("dicId") Long dicId) {
        try {
            return ResponseModel.success(roomMapper.roomClassDicToDto(roomClassDicService.getOne(dicId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@Valid @RequestBody RoomClassDicFilter filter) {
        try {
            return ResponseModel.success(roomMapper.roomClassDicToDto(roomClassDicService.addOne(filter)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{dicId}")
    public ResponseEntity<?> editOneById(@PathVariable("dicId") Long dicId,
                                         @Valid @RequestBody RoomClassDicFilter filter) {
        try {
            return ResponseModel.success(roomMapper.roomClassDicToDto(roomClassDicService.editOne(dicId, filter)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{dicId}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "dicId") Long dicId) {
        try{
            roomClassDicService.deleteOneById(dicId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
