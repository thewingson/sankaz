package kz.open.sankaz.repo;

import kz.open.sankaz.model.SecUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}
