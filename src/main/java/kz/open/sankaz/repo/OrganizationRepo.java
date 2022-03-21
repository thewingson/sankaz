package kz.open.sankaz.repo;

import kz.open.sankaz.model.Organization;
import kz.open.sankaz.model.SecUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepo extends CommonRepo<Organization> {
    Optional<Organization> findByTelNumber(@Param("telNumber") String telNumber);

    @Query(value = "select o " +
                    "from Organization o " +
                    "left join fetch o.sans s " +
                    "where o.user = :user")
    Optional<Organization> findByUser(@Param("user") SecUser user);
    Optional<Organization> findByIban(@Param("iban") String iban);
    Optional<Organization> findByIin(@Param("iin") String iin);

    List<Organization> findAllByConfirmationStatusIn(@Param("confirmationStatuses") List<String> confirmationStatuses);

    @Query(
            value = "select o.* " +
            "from organization o " +
            "left join company_category c on c.id = o.company_category_id " +
            "where " +
            "lower(o.name) like concat('%',:name,'%') " +
            "and lower(coalesce(o.company_name, '')) like concat('%',:companyName,'%') " +
            "and lower(coalesce(o.address, '')) like concat('%',:address,'%') " +
            "and lower(coalesce(c.code, '')) like concat('%',:companyCategoryCode,'%') " +
            "and lower(o.confirmation_status) like concat('%',:confirmationStatus,'%') ",
            nativeQuery = true
    )
    Page<Organization> findAllByFilters(@Param("name") String name,
                                        @Param("companyName") String companyName,
                                        @Param("address") String address,
                                        @Param("companyCategoryCode") String companyCategoryCode,
                                        @Param("confirmationStatus") String confirmationStatus,
                                        Pageable pageable);
}
