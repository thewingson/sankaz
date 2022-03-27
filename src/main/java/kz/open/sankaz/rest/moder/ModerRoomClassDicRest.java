package kz.open.sankaz.rest.moder;

import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.model.RoomClassDic;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.RoomClassDicService;
import kz.open.sankaz.service.SanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_MODERATOR')")
@RestController
@RequestMapping("/moders/room-class-dics")
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

    @GetMapping("/{dicId}/sans/{sanId}")
    public ResponseEntity<?> getOne(@PathVariable("dicId") Long dicId,
                                    @PathVariable("sanId") Long sanId) {
        try {
            RoomClassDic classDic = roomClassDicService.getOne(dicId, sanId);
            return ResponseModel.success(roomMapper.roomClassDicToDto(classDic));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

}
