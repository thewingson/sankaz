package kz.open.sankaz.rest.admin;

import kz.open.sankaz.exception.MessageCodeException;
import kz.open.sankaz.mapper.BookingMapper;
import kz.open.sankaz.pojo.filter.BookingAdminCreateFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/books")
public class AdminBookingRest {

    private final BookingService bookingService;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    public AdminBookingRest(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseModel.success(bookingMapper.bookingToBookingAdminDto(bookingService.getAll()));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getOne(@PathVariable("bookId") Long bookId) {
        try {
            return ResponseModel.success(bookingMapper.bookingToBookingAdminDto(bookingService.getOne(bookId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@Valid @RequestBody BookingAdminCreateFilter filter) {
        try {
            return ResponseModel.success(bookingMapper.bookingToBookingAdminDto(bookingService.addOne(filter)));
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<?> editOneById(@PathVariable("bookId") Long bookId,
                                         @Valid @RequestBody BookingAdminCreateFilter filter) {
        try {
            return ResponseModel.success(bookingMapper.bookingToBookingAdminDto(bookingService.editOneById(bookId, filter)));
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

}
