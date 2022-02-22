package kz.open.sankaz.repo;

import kz.open.sankaz.model.Room;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepo extends CommonRepo<Room> {
    List<Room> getAllByRoomClassDicId(@Param("classId") Long classId);
}
