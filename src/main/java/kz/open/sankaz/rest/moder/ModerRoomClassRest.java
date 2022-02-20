package kz.open.sankaz.rest.moder;

import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.service.RoomClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('ROLE_MODERATOR')")
@RestController
@RequestMapping("/moder/room-class")
public class ModerRoomClassRest {

    private final RoomClassService roomClassService;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    public ModerRoomClassRest(RoomClassService roomClassService) {
        this.roomClassService = roomClassService;
    }

//    @GetMapping("/sans/{sanId}")
//    public ResponseEntity<?> getAll(@PathVariable("sanId") Long sanId) {
////                method with check if own san's room class
//        try {
//            return ResponseModel.success(roomMapper.toDto(roomClassService.getAll()));
//        } catch (RuntimeException e) {
//            return ResponseModel.error(BAD_REQUEST, e.getMessage());
//        }
//    }
//
//    @GetMapping("/{classId}")
//    public ResponseEntity<?> getOne(@PathVariable("classId") Long classId) {
//        //        method with check if own san's room class
////        try {
////            return ResponseModel.success(roomMapper.toDto(roomClassService.getOne(classId)));
////        } catch (RuntimeException e) {
////            return ResponseModel.error(BAD_REQUEST, e.getMessage());
////        }
//    }
//
//    @PostMapping("/sans/{sanId}")
//    public ResponseEntity<?> addOne(@PathVariable("sanId") Long sanId,
//                                    @RequestBody RoomClassCreateFilter filter) {
//        //        method with check if own san
////        try {
////            return ResponseModel.success(roomMapper.toDto(roomClassService.addOne(filter)));
////        } catch (RuntimeException e) {
////            return ResponseModel.error(BAD_REQUEST, e.getMessage());
////        }
//    }
//
//    @PutMapping("/{classId}")
//    public ResponseEntity<?> editOneById(@PathVariable("classId") Long classId,
//                                         @RequestBody RoomClassCreateFilter filter) {
////        //        method with check if own san's room class
////        try {
////            return ResponseModel.success(roomMapper.toDto(roomClassService.editOneById(classId, filter)));
////        } catch (RuntimeException e) {
////            return ResponseModel.error(BAD_REQUEST, e.getMessage());
////        }
//    }
//
//    @DeleteMapping("/{classId}")
//    public ResponseEntity<?> deleteOneById(@PathVariable(name = "classId") Long classId) {
////        method with check if own san's room class
////        try{
////            roomClassService.deleteOneById(classId);
////            return ResponseModel.successPure();
////        } catch (Exception e){
////            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
////        }
//    }
//
//    @PostMapping("/pics")
//    public ResponseEntity<?> addOnePics() {
//        //        method with check if own san's room class
////        try {
//////            userService.updateProfile(userId, filter);
////            return ResponseModel.successPure();
////        } catch (RuntimeException e) {
////            return ResponseModel.error(BAD_REQUEST, e.getMessage());
////        }
//    }
//
//    @DeleteMapping("/pics/{picId}")
//    public ResponseEntity<?> deletePicById(@PathVariable(name = "picId") Long picId) {
////        method with check if own san's room class
////        try {
//////            userService.updateProfile(userId, filter);
////            return ResponseModel.successPure();
////        } catch (RuntimeException e) {
////            return ResponseModel.error(BAD_REQUEST, e.getMessage());
////        }
//    }

}
