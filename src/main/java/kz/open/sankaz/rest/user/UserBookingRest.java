package kz.open.sankaz.rest.user;

import kz.open.sankaz.exception.MessageCodeException;
import kz.open.sankaz.mapper.BookingMapper;
import kz.open.sankaz.pojo.dto.DatesDto;
import kz.open.sankaz.pojo.filter.BookingUserCreateFilter;
import kz.open.sankaz.pojo.filter.DateRangeFilter;
import kz.open.sankaz.repo.RoomRepo;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_USER')")
@RestController
@RequestMapping("/users")
public class UserBookingRest {

    private final BookingService bookingService;

    @Autowired
    private AuthService authService;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    public UserBookingRest(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/books/rooms/{roomId}")
    public ResponseEntity<?> getFreeDaysForBooking(HttpServletRequest request,
                                      @PathVariable("roomId") Long roomId,
                                      @Valid @RequestBody DateRangeFilter filter) {
        try {
            List<DatesDto> allFreeForBookingByDateRange = roomRepo.getRoomAvailabilityForDateRange(roomId, filter.getStartDate(), filter.getEndDate());
            return ResponseModel.success(allFreeForBookingByDateRange);
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/books")
    public ResponseEntity<?> bookRoom(HttpServletRequest request,
                                            @Valid @RequestBody BookingUserCreateFilter filter) {
        try {
            Long userId = authService.getUserId(request);
            return ResponseModel.success(bookingMapper.bookingToBookingAllForModerDto(bookingService.bookRoomFromUser(userId, filter)));
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getData(), e.getMessage());
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/books/history")
    public ResponseEntity<?> getHistory(HttpServletRequest request) {
        try {
            Long userId = authService.getUserId(request);
            return ResponseModel.success(bookingMapper.bookingToBookingUserHistoryDto(bookingService.getAllByUser(userId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<?> getById(@PathVariable("bookId") Long bookId) {
        try {
            return ResponseModel.success(bookingMapper.bookingToBookingByIdUserDto(bookingService.getOne(bookId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/books/{bookId}/cancel")
    public ResponseEntity<?> cancelBook(@PathVariable(name = "bookId") Long bookId) {
        try{
            bookingService.cancel(bookId, null);
            return ResponseModel.success("Заявка на отмену брони принято");
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/books/{bookId}/pay")
    public ResponseEntity<?> payBook(@PathVariable(name = "bookId") Long bookId) {
        try{
            return ResponseModel.success(bookingMapper.bookingToBookingModerByIdDto(bookingService.pay(bookId)));
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/books/{bookId}/payment-status")
    public ResponseEntity<?> paymentStatus(@PathVariable(name = "bookId") Long bookId) {
        try{
            return ResponseModel.success(bookingService.getPaymentPage(bookId));
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable(name = "bookId") Long bookId) {
        try{
            bookingService.deleteOneById(bookId);
            return ResponseModel.success(String.format("Срок брони %d истек", bookId));
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
