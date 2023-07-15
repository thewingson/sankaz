package kz.open.sankaz.migration.repo;

import kz.open.sankaz.migration.entity.RoomPics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomPicRepo extends CrudRepository<RoomPics,RoomPics> {
    @Query("select r from RoomPics r")
    List<RoomPics> findAll();
}
