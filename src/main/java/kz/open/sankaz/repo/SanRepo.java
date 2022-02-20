package kz.open.sankaz.repo;

import kz.open.sankaz.model.Organization;
import kz.open.sankaz.model.San;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanRepo extends CommonRepo<San>{

    List<San> getAllByOrganization(@Param("org")Organization organization);

    @Query(
            value = "SELECT * FROM SAN s " +
                    "WHERE s.CITY_ID = :cityId ",
            nativeQuery = true)
    List<San> getAllBySanForMainFilter(@Param("cityId") Long cityId);

//    @Query(
//            value = "SELECT * FROM SAN s " +
//                    "WHERE s.CITY_ID = :cityId ",
//            nativeQuery = true)
//    List<San> getAllBySanForMainFilter(@Param("cityId") Long cityId,
//                                       @Param("startDate") LocalDateTime startDate,
//                                       @Param("endDate") LocalDateTime endDate);

//    @Query(
//            value = "SELECT * FROM SAN s " +
//                    " IF :cityId IS NOT NULL THEN " +
//                    "   'INNER JOIN CITY c on c.id = s.CITY_ID AND c.ID = :cityId' " +
//                    " ELSE '' ",
//            nativeQuery = true)
//    List<San> getAllBySanForMainFilter(@Param("cityId") Long cityId,
//                                       @Param("startDate") LocalDateTime startDate,
//                                       @Param("endDate") LocalDateTime endDate);

//    @Query(
//            value = "SELECT * FROM SAN s " +
//                    "IF :filter.city JOIN CITY c on c.id = s.CITY_ID " +
//                    "WHERE s. ",
//            nativeQuery = true)
//    List<San> getAllBySanForMainFilter(@Param("filter") SanForMainFilter filter);

//    @Query(
//            value = "SELECT * FROM USERS u WHERE u.status = 1",
//            nativeQuery = true)
//    List<San> getAllBySanForMainFilter(@Param("filter") SanForMainFilter filter, Pageable pageable);
}
