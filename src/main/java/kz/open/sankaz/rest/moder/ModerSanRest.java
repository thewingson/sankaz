package kz.open.sankaz.rest.moder;

import kz.open.sankaz.mapper.NotificationMapper;
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

import static org.springframework.http.HttpStatus.BAD_REQUEST;

//@PreAuthorize("hasRole('ROLE_MODERATOR')")
@RestController
@RequestMapping("/moders/sans")
public class ModerSanRest {

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
    private NotificationMapper notificationMapper;

    @Autowired
    public ModerSanRest(SanService sanService) {
        this.sanService = sanService;
    }

    @GetMapping
    public ResponseEntity<?> getAllOwn() {
        try{
            return ResponseModel.success(sanService.getAllOwn());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{sanId}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "sanId") Long sanId) {
        try{
            //TODO
           // sanService.checkIfOwnSan(sanId);
            return ResponseModel.success(sanMapper.sanToSanByIdDto(sanService.getOne(sanId)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@RequestBody SanCreateFilter filter) {
        try{
            sanService.checkIfOwnOrg(filter.getOrgId());
            return ResponseModel.success(sanMapper.sanToDto(sanService.createSan(filter)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{sanId}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "sanId") Long sanId) {
        try{
            sanService.checkIfOwnSan(sanId);
            sanService.deleteOneById(sanId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "sanId") Long sanId,
                                         @Valid @RequestBody SanCreateFilter filter) {
        try{
            sanService.checkIfOwnSan(sanId);
            sanService.updateOneDto(sanId, filter);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/san-types")
    public ResponseEntity<?> changeSanType(@PathVariable(name = "sanId") Long sanId,
                                           @Valid @RequestBody SanAddDeleteTypesFilter filter) {
        try {
            sanService.checkIfOwnSan(sanId);
            return ResponseModel.success(sanMapper.sanToDto(sanService.changeSanType(sanId, filter.getSanTypeId())));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/tel-numbers/list")
    public ResponseEntity<?> addTelNumbers(@PathVariable(name = "sanId") Long sanId,
                                           @Valid @RequestBody SanAddDeleteNumbersFilter filter) {
        try {
            sanService.checkIfOwnSan(sanId);
            return ResponseModel.success(sanMapper.sanToDto(sanService.addTelNumbers(sanId, filter.getTelNumbers())));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{sanId}/tel-numbers/list")
    public ResponseEntity<?> deleteTelNumbers(@PathVariable(name = "sanId") Long sanId,
                                              @Valid @RequestBody SanAddDeleteNumbersFilter filter) {
        try {
            sanService.checkIfOwnSan(sanId);
            sanService.deleteTelNumbers(sanId, filter.getTelNumbers());
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }





    @GetMapping("/{sanId}/reviews")
    public ResponseEntity<?> getReviews(@PathVariable(name = "sanId") Long sanId,
                                        @RequestParam(value = "rating") int rating) {
        try{
            sanService.checkIfOwnSan(sanId);
            return ResponseModel.success(reviewService.getReviewInfoBySanId(sanId, rating));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{sanId}/reviews/filter")
    public ResponseEntity<?> getReviewsByFilter(@PathVariable(name = "sanId") Long sanId,
                                                @Valid @RequestBody ReviewBySanIdFilter filter) {
        try{
            sanService.checkIfOwnSan(sanId);
            return ResponseModel.success(reviewMapper.reviewToReviewBySanIdDto(reviewService.getAllByFilter(sanId, filter)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{sanId}/rooms/date-filter")
    public ResponseEntity<?> getAllByDate(@PathVariable(name = "sanId") Long sanId,
                                          @Valid @RequestBody DateRangeFilter filter) {
        try {
            return ResponseModel.success(roomMapper.roomToRoomForBookCreateDto(roomService.getAllByDate(sanId, filter.getStartDate(), filter.getEndDate())));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/reviews")
    public ResponseEntity<?> answerToReview(@PathVariable(name = "sanId") Long sanId,
                                       @Valid @RequestBody ReviewModerCreateFilter filter) {
        try {
            return ResponseModel.success(reviewMapper.reviewToReviewCreateDto(sanService.answerToReview(sanId, filter)));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{sanId}/stock")
    public ResponseEntity<?> addStock(@PathVariable(name = "sanId") Long sanId,
                                       @Valid @RequestBody StockCreateFilter filter) {
        try {
            return ResponseModel.success(notificationMapper.stockToDto(sanService.addStock(sanId, filter)));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/stock/{stockId}")
    public ResponseEntity<?> editStock(@PathVariable(name = "stockId") Long stockId,
                                      @Valid @RequestBody StockCreateFilter filter) {
        try {
            return ResponseModel.success(notificationMapper.stockToDto(sanService.editStock(stockId, filter)));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/stock/{stockId}")
    public ResponseEntity<?> deleteStock(@PathVariable(name = "stockId") Long stockId) {
        try {
            sanService.deleteStock(stockId);
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
