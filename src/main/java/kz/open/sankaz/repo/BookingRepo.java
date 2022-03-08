package kz.open.sankaz.repo;

import kz.open.sankaz.model.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepo extends CommonRepo<Booking> {

    @Query("select b from Booking b where b.room.roomClassDic.san.id = :sanId and b.status <> 'CANCELLED'")
    List<Booking> getAllBySanId(@Param("sanId") Long sanId);

    @Query("select b " +
            "from Booking b " +
            "where b.room.id = :roomId " +
            "and b.status <> 'CANCELLED' and b.status <> 'WAITING' " +
            "and ((:startDate between b.startDate and b.endDate) or (:endDate between b.startDate and b.endDate))")
    List<Booking> getAllByRoomIdAndDateRanges(@Param("roomId") Long roomId,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query(value = "select b.* from booking b where b.user_id = :userId and b.status = :status", nativeQuery = true)
    List<Booking> getAllByUserIdAndStatus(@Param("userId") Long userId,
                                          @Param("status") String status);

    @Query("select b " +
            "from Booking b " +
            "where b.user.id = :userId " +
            "and b.status <> kz.open.sankaz.model.enums.BookingStatus.CANCELLED " +
            "and b.status <> kz.open.sankaz.model.enums.BookingStatus.PAID ")
    List<Booking> getAllActiveByUserId(Long userId);

    @Query("select b " +
            "from Booking b " +
            "where b.user.id = :userId " +
            "and (b.status = kz.open.sankaz.model.enums.BookingStatus.CANCELLED " +
            "     or b.status = kz.open.sankaz.model.enums.BookingStatus.PAID) ")
    List<Booking> getAllHistoryByUserId(Long userId);

    List<Booking> getAllByUserId(@Param("userId") Long userId);

    @Query(value = "select b.* " +
            "from booking b " +
            "where " +
            "b.room_id in :roomIds " +
            "and b.status <> 'CANCELLED' and b.status <> 'WAITING' " +
            "and ((cast(b.start_date as date) between :startDate and :endDate) " +
            "  or (cast(b.end_date as date) between :startDate and :endDate))",
            nativeQuery = true)
    List<Booking> getBookingCalendar(@Param("roomIds") List<Long> roomIds,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);
}
