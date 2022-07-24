package kz.open.sankaz.repo;

import kz.open.sankaz.model.BookingHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingHistoryRepo extends CommonRepo<BookingHistory> {

    Page<BookingHistory> findAllByBookingId(@Param("bookingId")Long bookingId,
                                            Pageable pageable);

}
