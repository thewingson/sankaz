package kz.open.sankaz.repo;

import kz.open.sankaz.model.SecUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends CommonRepo<SecUser> {
    Optional<SecUser> findByUsername(@Param("username") String username);
    Optional<SecUser> findByEmail(@Param("email") String email);
    Optional<SecUser> findByTelNumber(@Param("telNumber") String telNumber);
    SecUser getByUsername(@Param("username") String username);
    List<SecUser> getAllByActive(@Param("active") boolean active);
    Page<SecUser> getAllByActive(@Param("active") boolean active, Pageable pageable);

    @Query(value = "select e.* " +
            "from sec_user e " +
            "left join organization o on o.user_id = e.id " +
            "where o.id is null " +
            "and e.active = ?1 " +
            "and e.user_type = 'ORG' " +
            "and e.tel_number like concat('%',?3,'%')" +
            "and (lower(concat(e.first_name,' ', e.last_name)) like concat('%',?2,'%') " +
            "       or lower(concat(e.last_name, ' ', e.first_name)) like concat('%',?2,'%')) " +
            "limit ?5 offset ?4 ",
            nativeQuery = true)
    List<SecUser> getAllForNewOrganization(boolean active,
                                        String fullName,
                                        String telNumber,
                                        int page,
                                        int size);
}
