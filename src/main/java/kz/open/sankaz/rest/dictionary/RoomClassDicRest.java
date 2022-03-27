package kz.open.sankaz.rest.dictionary;

import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.RoomClassDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/dict/room-class-dics")
public class RoomClassDicRest {

    private final RoomClassDicService roomClassDicService;
    private final RoomMapper roomMapper;

    @Autowired
    public RoomClassDicRest(RoomClassDicService roomClassDicService, RoomMapper roomMapper) {
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

}
