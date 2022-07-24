package kz.open.sankaz.rest.admin;

import kz.open.sankaz.exception.MessageCodeException;
import kz.open.sankaz.mapper.BookingMapper;
import kz.open.sankaz.model.Booking;
import kz.open.sankaz.repo.BookingRepo;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/books")
public class AdminBookingRest {

    private final BookingService bookingService;

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    public AdminBookingRest(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/filter")
    public ResponseEntity<?> getAllBooksBySan(@RequestParam(required = false) Long sanId,
                                              @RequestParam(required = false) String telNumber,
                                              @RequestParam(required = false) String status,
                                              @RequestParam(required = false) BigDecimal minPrice,
                                              @RequestParam(required = false) BigDecimal maxPrice,
                                              @RequestParam(value="startDate", required = false) String startDate,
                                              @RequestParam(value="endDate", required = false) String endDate,
                                              @RequestParam(defaultValue = "0") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            List<Booking> bookings = bookingRepo.getBookingByFilter(
                    startDate.isEmpty() ? null : LocalDate.parse(startDate, formatter),
                    endDate.isEmpty() ? null : LocalDate.parse(endDate, formatter),
                    sanId,
                    telNumber,
                    status.toLowerCase(),
                    minPrice,
                    maxPrice,
                    page,
                    size);
            return ResponseModel.success(bookingMapper.bookingToBookingAdminDto(bookings));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{bookId}/transfer")
    public ResponseEntity<?> transfer(@PathVariable(name = "bookId") Long bookId) {
        try{
            return ResponseModel.success(bookingMapper.bookingToBookingModerByIdDto(bookingService.transfer(bookId)));
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{bookId}/history")
    public ResponseEntity<?> getHistory(@PathVariable(name = "bookId") Long bookId) {
        try{
            return ResponseModel.success(bookingMapper.bookingHistoryToDto(bookingService.getHistory(bookId)));
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
