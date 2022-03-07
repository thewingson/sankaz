package kz.open.sankaz.rest.moder;

import kz.open.sankaz.exception.MessageCodeException;
import kz.open.sankaz.mapper.BookingMapper;
import kz.open.sankaz.pojo.filter.BookingModerCreateFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_MODERATOR')")
@RestController
@RequestMapping("/moders/books")
public class ModerBookingRest {

    private final BookingService bookingService;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    public ModerBookingRest(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/sans/{sanId}")
    public ResponseEntity<?> getAllBySan(@PathVariable("sanId") Long sanId) {
        try {
            return ResponseModel.success(bookingMapper.bookingToBookingAllForModerDto(bookingService.getAllBySan(sanId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getOne(@PathVariable("bookId") Long bookId) {
        try {
            return ResponseModel.success(bookingMapper.bookingToBookingModerByIdDto(bookingService.getOne(bookId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@Valid @RequestBody BookingModerCreateFilter filter) {
        try {
            return ResponseModel.success(bookingMapper.bookingToBookingModerByIdDto(bookingService.addOne(filter)));
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<?> editOneById(@PathVariable("bookId") Long bookId,
                                         @Valid @RequestBody BookingModerCreateFilter filter) {
        try {
            return ResponseModel.success(bookingMapper.bookingToBookingModerByIdDto(bookingService.editOneById(bookId, filter)));
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "bookId") Long bookId) {
        try{
            bookingService.deleteOneById(bookId);
            return ResponseModel.successPure();
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{bookId}/approve")
    public ResponseEntity<?> approveBook(@PathVariable(name = "bookId") Long bookId) {
        try{
            return ResponseModel.success(bookingMapper.bookingToBookingModerByIdDto(bookingService.approve(bookId)));
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getData(), e.getMessage());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{bookId}/cancel")
    public ResponseEntity<?> cancelBook(@PathVariable(name = "bookId") Long bookId) {
        try{
            return ResponseModel.success(bookingMapper.bookingToBookingModerByIdDto(bookingService.cancel(bookId)));
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{bookId}/pay")
    public ResponseEntity<?> payBook(@PathVariable(name = "bookId") Long bookId) {
        try{
            return ResponseModel.success(bookingMapper.bookingToBookingModerByIdDto(bookingService.pay(bookId)));
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
