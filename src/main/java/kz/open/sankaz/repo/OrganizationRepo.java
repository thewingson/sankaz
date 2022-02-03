package kz.open.sankaz.repo;

import kz.open.sankaz.model.Organization;
import kz.open.sankaz.model.SecUser;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepo extends CommonRepo<Organization> {
    Optional<Organization> findByTelNumberAndDeletedByIsNull(@Param("telNumber") String telNumber);
    Optional<Organization> findByUserAndDeletedByIsNull(@Param("user") SecUser user);
    Optional<Organization> findByIbanAndDeletedByIsNull(@Param("iban") String iban);
    Optional<Organization> findByIinAndDeletedByIsNull(@Param("iin") String iin);

    List<Organization> findAllByConfirmationStatusInAndDeletedByIsNull(@Param("confirmationStatuses") List<String> confirmationStatuses);
}
