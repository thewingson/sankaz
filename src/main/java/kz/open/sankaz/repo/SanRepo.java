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
                    "join room r on " +
                    "            case when (cast(:personCount as numeric) is null) then r.class_id = dic.id  " +
                    "            else r.class_id = dic.id and r.bed_count >= cast(:personCount as numeric) " +
                    "            end " +
                    "left join booking b on" +
                    "                    case when (cast(:startDate as date) is null) then false " +
                    "                    else b.room_id = r.id and b.status <> 'CANCELLED' and b.status <> 'WAITING' " +
                    "                    and ((cast(b.start_date as date) between cast(:startDate as date) and cast(:endDate as date)) " +
                    "                    or (cast(b.end_date as date) between cast(:startDate as date) and cast(:endDate as date))) " +
                    "                    end " +
                    "where 1=1 " +
                    "and case when (cast(:startDate as date) is not null) " +
                    "    then b.id is null " +
                    "    else 1=1 end " +
                    "and case " +
                    "    when (cast(:cityId as numeric) is not null) " +
                    "    then s.city_id = cast(:cityId as numeric) " +
                    "    else 1=1 end" +
                    "    group by s.id;",
            nativeQuery = true)
    List<San> getAllBySanForMainFilter(@Param("cityId") Long cityId,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate,
                                       @Param("personCount") Integer personCount);
}
