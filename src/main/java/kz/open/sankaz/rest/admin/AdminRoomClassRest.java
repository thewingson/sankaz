package kz.open.sankaz.rest.admin;

import kz.open.sankaz.mapper.FileMapper;
import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.pojo.filter.DeletePicsFilter;
import kz.open.sankaz.pojo.filter.RoomClassCreateFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.RoomClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/admin/room-class")
public class AdminRoomClassRest {

    private final RoomClassService roomClassService;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    public AdminRoomClassRest(RoomClassService roomClassService) {
        this.roomClassService = roomClassService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseModel.success(roomMapper.roomClassToDto(roomClassService.getAll()));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{classId}")
    public ResponseEntity<?> getOne(@PathVariable("classId") Long classId) {
        try {
            return ResponseModel.success(roomMapper.roomClassToDto(roomClassService.getOne(classId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@Valid @RequestBody RoomClassCreateFilter filter) {
        try {
            return ResponseModel.success(roomMapper.roomClassToDto(roomClassService.addOne(filter)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{classId}")
    public ResponseEntity<?> editOneById(@PathVariable("classId") Long classId,
                                         @Valid @RequestBody RoomClassCreateFilter filter) {
        try {
            return ResponseModel.success(roomMapper.roomClassToDto(roomClassService.editOneById(classId, filter)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{classId}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "classId") Long classId) {
        try{
            roomClassService.deleteOneById(classId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{classId}/pics/list")
    public ResponseEntity<?> addPics(@PathVariable(name = "classId") Long classId,
                                     @RequestParam("pics") MultipartFile[] pics) {
        try {
            return ResponseModel.success(fileMapper.fileToFileUrlDto((roomClassService.addPics(classId, pics))));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{classId}/pics/list")
    public ResponseEntity<?> deletePics(@PathVariable(name = "classId") Long classId,
                                        @Valid @RequestBody DeletePicsFilter filter) {
        try {
            roomClassService.deletePics(classId, filter.getPicIds());
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
