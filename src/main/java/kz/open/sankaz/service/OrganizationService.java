package kz.open.sankaz.service;

import kz.open.sankaz.dto.OrganizationDto;
import kz.open.sankaz.dto.OrganizationFilterDto;
import kz.open.sankaz.model.Organization;
import kz.open.sankaz.model.SecUser;

import java.util.List;

public interface OrganizationService extends CommonService<Organization>, CommonDtoService<Organization, OrganizationDto> {
    Organization getOrganizationByTelNumber(String telNumber);
    Organization getOrganizationByUser(SecUser user);

    Organization getOrganizationByIban(String iban);

    Organization getOrganizationByIin(String iin);

    List<OrganizationDto> getAllByConfirmationStatuses(OrganizationFilterDto organizationFilter);

    void approveOrganizationData(Long orgId);
    void rejectOrganizationData(Long orgId);

}
