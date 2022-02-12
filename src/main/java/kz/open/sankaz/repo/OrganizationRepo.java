package kz.open.sankaz.repo;

import kz.open.sankaz.model.Organization;
import kz.open.sankaz.model.SecUser;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepo extends CommonRepo<Organization> {
    Optional<Organization> findByTelNumber(@Param("telNumber") String telNumber);
    Optional<Organization> findByUser(@Param("user") SecUser user);
    Optional<Organization> findByIban(@Param("iban") String iban);
    Optional<Organization> findByIin(@Param("iin") String iin);

    List<Organization> findAllByConfirmationStatusIn(@Param("confirmationStatuses") List<String> confirmationStatuses);
}
