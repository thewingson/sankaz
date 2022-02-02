package kz.open.sankaz.service;

import kz.open.sankaz.model.Organization;
import kz.open.sankaz.model.SecUser;

public interface OrganizationService extends CommonService<Organization> {
    Organization getOrganizationByTelNumber(String telNumber);
    Organization getOrganizationByUser(SecUser user);

    Organization getOrganizationByIban(String iban);

    Organization getOrganizationByIin(String iin);
}
