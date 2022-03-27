package kz.open.sankaz.rest.admin.dictionary;

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
@RequestMapping("/admin/dict/room-class-dics")
public class AdminRoomClassDicRest {

    private final RoomClassDicService roomClassDicService;
    private final RoomMapper roomMapper;

    @Autowired
    public AdminRoomClassDicRest(RoomClassDicService roomClassDicService, RoomMapper roomMapper) {
        this.roomClassDicService = roomClassDicService;
        this.roomMapper = roomMapper;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseModel.success(roomMapper.roomClassDicSimpleToDto(roomClassDicService.getAll()));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{classId}")
    public ResponseEntity<?> getOne(@PathVariable("classId") Long classId) {
        try {
            return ResponseModel.success(roomMapper.roomClassDicSimpleToDto(roomClassDicService.getOne(classId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@Valid @RequestBody RoomClassDicFilter filter) {
        try{
            return ResponseModel.success(roomClassDicService.addOne(filter));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{classId}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "classId") Long classId) {
        try{
            roomClassDicService.deleteOneById(classId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{classId}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "classId") Long classId,
                                         @Valid @RequestBody RoomClassDicFilter filter) {
        try{
            return ResponseModel.success(roomClassDicService.editOne(classId, filter));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
