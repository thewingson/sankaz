package kz.open.sankaz.rest.user;

import kz.open.sankaz.exception.MessageCodeException;
import kz.open.sankaz.mapper.BookingMapper;
import kz.open.sankaz.pojo.dto.DatesDto;
import kz.open.sankaz.pojo.filter.BookingUserCreateFilter;
import kz.open.sankaz.pojo.filter.BookingUserGetFilter;
import kz.open.sankaz.pojo.filter.DateRangeFilter;
import kz.open.sankaz.repo.BookingRepo;
import kz.open.sankaz.repo.RoomRepo;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_USER')")
@RestController
@RequestMapping("/users")
public class UserBookingRest {

    private final BookingService bookingService;

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    public UserBookingRest(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/{userId}/books/rooms/{roomId}")
    public ResponseEntity<?> getFreeDaysForBooking(@PathVariable("userId") Long userId,
                                      @PathVariable("roomId") Long roomId,
                                      @Valid @RequestBody DateRangeFilter filter) {
        try {
            List<DatesDto> allFreeForBookingByDateRange = roomRepo.getRoomAvailabilityForDateRange(roomId, filter.getStartDate(), filter.getEndDate());
            return ResponseModel.success(allFreeForBookingByDateRange);
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{userId}/books")
    public ResponseEntity<?> bookRoom(@PathVariable("userId") Long userId,
                                            @Valid @RequestBody BookingUserCreateFilter filter) {
        try {
            return ResponseModel.success(bookingMapper.bookingToBookingAllForModerDto(bookingService.bookRoomFromUser(userId, filter)));
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getData(), e.getMessage());
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{userId}/books/filter")
    public ResponseEntity<?> getAllByFilter(@PathVariable("userId") Long userId,
                                            @Valid @RequestBody BookingUserGetFilter filter) {
        try {
            return ResponseModel.success(bookingMapper.bookingToBookingAllForModerDto(bookingRepo.getAllByUserIdAndStatus(userId, filter.getStatus())));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{userId}/books/active")
    public ResponseEntity<?> getAllActive(@PathVariable("userId") Long userId) {
        try {
            return ResponseModel.success(bookingMapper.bookingToBookingUserDto(bookingRepo.getAllActiveByUserId(userId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{userId}/books/history")
    public ResponseEntity<?> getAllHistory(@PathVariable("userId") Long userId) {
        try {
            return ResponseModel.success(bookingMapper.bookingToBookingUserDto(bookingRepo.getAllHistoryByUserId(userId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{userId}/books/{bookId}")
    public ResponseEntity<?> getById(@PathVariable("userId") Long userId,
                                     @PathVariable("bookId") Long bookId) {
        try {
            return ResponseModel.success(bookingMapper.bookingToBookingByIdUserDto(bookingService.getOne(bookId)));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{userId}/books/{bookId}/cancel")
    public ResponseEntity<?> cancelBook(@PathVariable("userId") Long userId,
                                        @PathVariable(name = "bookId") Long bookId) {
        try{
            return ResponseModel.success(bookingMapper.bookingToBookingModerByIdDto(bookingService.cancel(bookId)));
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{userId}/books/{bookId}/pay")
    public ResponseEntity<?> payBook(@PathVariable("userId") Long userId,
                                     @PathVariable(name = "bookId") Long bookId) {
        try{
            return ResponseModel.success(bookingMapper.bookingToBookingModerByIdDto(bookingService.pay(bookId)));
        } catch (MessageCodeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
