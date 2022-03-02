package kz.open.sankaz.repo;

import kz.open.sankaz.model.Organization;
import kz.open.sankaz.model.San;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SanRepo extends CommonRepo<San>{

    List<San> getAllByOrganization(@Param("org")Organization organization);

    @Query(
            value = "select s.* " +
                    "from san s " +
                    "join room_class_dic dic on dic.san_id = s.id " +
                    "join room r on r.class_id = dic.id " +
                    "left join booking b on b.room_id = r.id " +
                    "and b.status <> 'CANCELLED' " +
                    "and b.status <> 'WAITING' " +
                    "and ((cast(b.start_date as date) between cast(:startDate as date) and cast(:endDate as date)) " +
                    "       or (cast(b.end_date as date) between cast(:startDate as date) and cast(:endDate as date))) " +
                    "where b.id is null " +
                    "and s.city_id = :cityId " +
                    "group by s.id ;",
            nativeQuery = true)
    List<San> getAllBySanForMainFilter(@Param("cityId") Long cityId,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);
}
