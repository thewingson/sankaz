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
                    "join san_type st on st.id = s.san_type_id " +
                    "join room r on " +
                    "            case when (cast(cast(:personCount as text) as numeric) is null) then r.san_id = s.id  " +
                    "            else r.bed_count >= cast(cast(:personCount as text) as numeric) " +
                    "            end " +
                    "left join booking b on" +
                    "                    case when (cast(cast(:startDate as text) as date) is null) then false " +
                    "                    else b.room_id = r.id and b.status <> 'CANCELLED' and b.status <> 'WAITING' " +
                    "                    and ((cast(b.start_date as date) between cast(cast(:startDate as text) as date) and cast(cast(:endDate as text) as date)) " +
                    "                    or (cast(b.end_date as date) between cast(cast(:startDate as text) as date) and cast(cast(:endDate as text) as date))) " +
                    "                    end " +
                    "where 1=1 " +
                    "and case when :name is not null " +
                    "    then lower(s.name) like concat('%', :name, '%') " +
                    "    else 1=1 end " +
                    "and case when :sanTypeCode is not null " +
                    "    then lower(st.code) like concat('%', :sanTypeCode, '%') " +
                    "    else 1=1 end " +
                    "and case when (cast(cast(:startDate as text) as date) is not null) " +
                    "    then b.id is null " +
                    "    else 1=1 end " +
                    "and case " +
                    "    when (cast(cast(:cityId as text) as numeric) is not null) " +
                    "    then s.city_id = cast(cast(:cityId as text) as numeric) " +
                    "    else 1=1 end" +
                    "    group by s.id " +
                    " limit cast(cast(:size as text) as numeric) offset cast(cast(:page as text) as numeric);",
            nativeQuery = true)
    List<San> getAllBySanForMainFilter(@Param("cityId") Long cityId,
                                       @Param("name") String name,
                                       @Param("sanTypeCode") String sanTypeCode,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate,
                                       @Param("personCount") Integer personCount,
                                       @Param("page") Integer page,
                                       @Param("size") Integer size);

    @Query(
            value = "select s.* " +
                    "from san s " +
                    "join san_type st on st.id = s.san_type_id " +
                    "where 1=1 " +
                    "and case when :name is not null " +
                    "    then lower(s.name) like concat('%', :name, '%') " +
                    "    else 1=1 end " +
                    "and case when :sanTypeCode is not null " +
                    "    then lower(st.code) like concat('%', :sanTypeCode, '%') " +
                    "    else 1=1 end " +
                    "and case " +
                    "    when (cast(cast(:cityId as text) as numeric) is not null) " +
                    "    then s.city_id = cast(cast(:cityId as text) as numeric) " +
                    "    else 1=1 end" +
                    "    group by s.id " +
                    " limit cast(cast(:size as text) as numeric) offset cast(cast(:page as text) as numeric);",
            nativeQuery = true)
    List<San> getAllBySanForMainAdminFilter(@Param("cityId") Long cityId,
                                       @Param("name") String name,
                                       @Param("sanTypeCode") String sanTypeCode,
                                       @Param("page") Integer page,
                                       @Param("size") Integer size);

    @Query(
            value = "select s.* " +
                    "from san s " +
                    "join USER_FAVORITES f on f.san_id = s.id " +
                    "join sec_user u on u.id = f.user_id and u.id = :userId" +
                    " limit cast(cast(:size as text) as numeric) offset cast(cast(:page as text) as numeric);",
            nativeQuery = true)
    List<San> getFavs(@Param("userId") Long userId,
                      @Param("page") Integer page,
                      @Param("size") Integer size);

    @Query(value = "select s.id from san s " +
            "join USER_FAVORITES f on f.san_id = s.id " +
            "join sec_user u on u.id = f.user_id and u.id = :userId ",
            nativeQuery = true)
    List<Long> getFavSanId(@Param("userId") Long userId);
}
