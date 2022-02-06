package kz.open.sankaz.repo;

import kz.open.sankaz.model.SecUser;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends CommonRepo<SecUser> {
    Optional<SecUser> findByUsername(@Param("username") String username);
    Optional<SecUser> findByEmailAndDeletedByIsNull(@Param("email") String email);
    Optional<SecUser> findByTelNumber(@Param("telNumber") String telNumber);
    SecUser findByDeletedByIsNullAndUsername(@Param("username") String username);
//    SecUser findByConfirmationId(@Param("confirmationId") UUID confirmationId);
}
