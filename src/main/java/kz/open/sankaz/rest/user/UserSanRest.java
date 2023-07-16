package kz.open.sankaz.rest.user;

import kz.open.sankaz.mapper.ReviewMapper;
import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.mapper.SanMapper;
import kz.open.sankaz.model.RoomClassDic;
import kz.open.sankaz.pojo.dto.RoomClassDicDto;
import kz.open.sankaz.pojo.filter.ReviewCreateFilter;
import kz.open.sankaz.pojo.filter.SanForMainFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_USER')")
@RestController
@RequestMapping("/users/sans")
public class UserSanRest {

    private final SanService sanService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomClassDicService roomClassDicService;

    @Autowired
    private AuthService authService;



    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    public UserSanRest(SanService sanService) {
        this.sanService = sanService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<?> getAllSanatorium() {
        try{
            return ResponseModel.success(sanService.getAllForMain());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{sanId}/classes")
    public ResponseEntity<?> getAllClassesBySan(@PathVariable(name = "sanId") Long sanId) {
        try {
            List<RoomClassDicDto> asd=roomMapper.roomClassDicToDto(roomClassDicService.getBySan(sanId));
            return ResponseModel.success(asd);
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/reviews")
    public ResponseEntity<?> addReview(@PathVariable(name = "sanId") Long sanId,
                                       @Valid @RequestBody ReviewCreateFilter filter) {
        try {
            return ResponseModel.success(reviewMapper.reviewToReviewCreateDto(sanService.addReview(sanId, filter)));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{sanId}/reviews/filter")
    public ResponseEntity<?> getReviewsByFilter(@PathVariable(name = "sanId") Long sanId,
                                                @RequestParam(defaultValue = "0") int rating) {
        try{
            return ResponseModel.success(reviewService.getReviewInfoBySanId(sanId, rating));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{sanId}/rooms/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable(name = "sanId") Long sanId,
                                     @PathVariable(name = "roomId") Long roomId) {
        try{
            return ResponseModel.success(roomMapper.roomToRoomByIdDtoForUser(roomService.getOne(roomId)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
