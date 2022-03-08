package kz.open.sankaz.repo.dictionary;

import kz.open.sankaz.model.RoomClassDic;
import kz.open.sankaz.repo.CommonRepo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomClassDicRepo extends CommonRepo<RoomClassDic> {

    @Query("select distinct d from RoomClassDic d " +
            "join fetch d.rooms r " +
            "where d.san.id = :sanId")
    List<RoomClassDic> getRoomClassDicBySanId(@Param("sanId") Long sanId);

}
