package kz.open.sankaz.rest.user;

import kz.open.sankaz.mapper.ReviewMapper;
import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.mapper.SanMapper;
import kz.open.sankaz.pojo.filter.ReviewCreateFilter;
import kz.open.sankaz.pojo.filter.SanForMainFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.ReviewService;
import kz.open.sankaz.service.RoomClassDicService;
import kz.open.sankaz.service.RoomService;
import kz.open.sankaz.service.SanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private SanMapper sanMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    public UserSanRest(SanService sanService) {
        this.sanService = sanService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Long cityId,
            @RequestParam(value="startDate", required = false) String startDate,
            @RequestParam(value="endDate", required = false) String endDate,
            @RequestParam(required = false) Integer adults,
            @RequestParam(required = false) Integer children,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            SanForMainFilter filter = new SanForMainFilter(cityId,
                    startDate.isEmpty() ? null : LocalDateTime.parse(startDate, formatter),
                    endDate.isEmpty() ? null : LocalDateTime.parse(endDate, formatter), adults, children);
            return ResponseModel.success(sanService.getAllForMain(filter, page, size));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{sanId}/classes")
    public ResponseEntity<?> getAllClassesBySan(@PathVariable(name = "sanId") Long sanId) {
        try {
            return ResponseModel.success(roomMapper.roomClassDicToDto(roomClassDicService.getBySan(sanId)));
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
