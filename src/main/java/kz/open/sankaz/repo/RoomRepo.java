package kz.open.sankaz.repo;

import kz.open.sankaz.model.Room;
import kz.open.sankaz.pojo.dto.DatesDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepo extends CommonRepo<Room> {
    List<Room> getAllByRoomClassDicId(@Param("classId") Long classId);

    List<Room> getAllByRoomClassDicIdAndSanId(@Param("classId") Long classId, @Param("sanId") Long sanId);

    @Query("select distinct r " +
            "from Room r " +
            "join fetch r.roomClassDic d " +
            "join r.san s on s.id = :sanId " +
            "left join Booking b on b.room.id = r.id " +
            "where b is null " +
            "or b.status <> 'CANCELLED' or ((:startDate > b.endDate and :endDate > b.endDate) or (:startDate < b.startDate and :endDate < b.startDate))")
    List<Room> getAllFreeForBookingByDateRange(@Param("sanId") Long sanId,
                                                       @Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate);

    @Query(nativeQuery = true)
    List<DatesDto> getRoomAvailabilityForDateRange(@Param("roomId") Long roomId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    @Query(nativeQuery = true,
            value = "select r.* " +
            "from room r " +
            "join san s on s.id = r.san_id and s.id = :sanId " +
            "where lower(r.room_number) = :roomNumber")
    List<Room> findRoomByNameAndSan(@Param("sanId") Long sanId,
                                    @Param("roomNumber") String roomNumber);

}
