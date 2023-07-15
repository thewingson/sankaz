package kz.open.sankaz.migration.repo;

import kz.open.sankaz.migration.entity.RoomPics;
import kz.open.sankaz.migration.entity.SanPics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPicRepo extends CrudRepository<SanPics,SanPics> {
    @Query("select s from SanPics s")
    List<SanPics> findAll();
}
