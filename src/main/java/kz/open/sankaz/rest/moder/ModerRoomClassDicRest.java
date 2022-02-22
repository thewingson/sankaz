package kz.open.sankaz.rest.moder;

import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.model.RoomClassDic;
import kz.open.sankaz.pojo.filter.RoomClassDicFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.RoomClassDicService;
import kz.open.sankaz.service.SanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_MODERATOR')")
@RestController
@RequestMapping("/moder/room-class-dics")
public class ModerRoomClassDicRest {

    private final RoomClassDicService roomClassDicService;

    @Autowired
    private SanService sanService;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    public ModerRoomClassDicRest(RoomClassDicService roomClassDicService) {
        this.roomClassDicService = roomClassDicService;
    }

    @GetMapping("/sans/{sanId}")
    public ResponseEntity<?> getAllBySan(@PathVariable(name = "sanId") Long sanId) {
        try {
            sanService.checkIfOwnSan(sanId);
            return ResponseModel.success(roomMapper.roomClassDicToDto(roomClassDicService.getBySan(sanId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{dicId}")
    public ResponseEntity<?> getOne(@PathVariable("dicId") Long dicId) {
        try {
            RoomClassDic classDic = roomClassDicService.getOne(dicId);
            sanService.checkIfOwnSan(classDic.getSan().getId());
            return ResponseModel.success(roomMapper.roomClassDicToDto(classDic));
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
