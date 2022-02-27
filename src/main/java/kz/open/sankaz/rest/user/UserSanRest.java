package kz.open.sankaz.rest.user;

import kz.open.sankaz.mapper.ReviewMapper;
import kz.open.sankaz.mapper.RoomMapper;
import kz.open.sankaz.mapper.SanMapper;
import kz.open.sankaz.pojo.filter.*;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.ReviewService;
import kz.open.sankaz.service.RoomService;
import kz.open.sankaz.service.SanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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
    private SanMapper sanMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    public UserSanRest(SanService sanService) {
        this.sanService = sanService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @RequestBody SanForMainFilter filter) {
        try{
            return ResponseModel.success(sanService.getAllForMain(filter));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{sanId}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "sanId") Long sanId) {
        try{
            return ResponseModel.success(sanMapper.sanToSanByIdDto(sanService.getOne(sanId)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
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

    @GetMapping("/{sanId}/reviews")
    public ResponseEntity<?> getReviews(@PathVariable(name = "sanId") Long sanId) {
        try{
            return ResponseModel.success(reviewMapper.reviewToReviewBySanIdDto(reviewService.getAllBySanId(sanId)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{sanId}/reviews/filter")
    public ResponseEntity<?> getReviewsByFilter(@PathVariable(name = "sanId") Long sanId, @Valid @RequestBody ReviewBySanIdFilter filter) {
        try{
            return ResponseModel.success(reviewMapper.reviewToReviewBySanIdDto(reviewService.getAllByFilter(sanId, filter)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

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
