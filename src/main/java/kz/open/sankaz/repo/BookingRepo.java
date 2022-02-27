package kz.open.sankaz.repo;

import kz.open.sankaz.model.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
