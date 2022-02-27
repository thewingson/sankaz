package kz.open.sankaz.repo;

import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.RoomClassDic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepo extends CommonRepo<Room> {
    List<Room> getAllByRoomClassDicId(@Param("classId") Long classId);

    @Query("select distinct r " +
            "from Room r " +
            "join fetch r.roomClassDic d " +
            "join d.san s on s.id = :sanId " +
            "left join Booking b on b.room.id = r.id " +
            "where b is null " +
            "or b.status <> 'CANCELLED' or ((:startDate > b.endDate and :endDate > b.endDate) or (:startDate < b.startDate and :endDate < b.startDate))")
    List<Room> getAllFreeForBookingByDateRange(@Param("sanId") Long sanId,
                                                       @Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate);
}
