package kz.open.sankaz.repo;

import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.San;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepo extends CommonRepo<Room> {

    List<Room> getAllBySan(@Param("san") San san);

}
