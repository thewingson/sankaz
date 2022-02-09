package kz.open.sankaz.service;

import kz.open.sankaz.model.Organization;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.pojo.dto.OrganizationDto;
import kz.open.sankaz.pojo.filter.OrganizationEditFilter;
import kz.open.sankaz.pojo.filter.OrganizationFilterFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OrganizationService extends CommonService<Organization>, CommonDtoService<Organization, OrganizationDto> {
    Organization getOrganizationByTelNumber(String telNumber);
    Organization getOrganizationByUser(SecUser user);

    Organization getOrganizationByIban(String iban);

    Organization getOrganizationByIin(String iin);

    List<Organization> getAllByConfirmationStatuses(OrganizationFilterFilter filter);

    void approveOrganizationData(Long orgId);
    void rejectOrganizationData(Long orgId);

    void finishProfile(Long orgId, OrganizationEditFilter filter);

    void uploadPicture(Long orgId, MultipartFile pic) throws IOException;
    void uploadPicture(Long orgId, MultipartFile[] pic) throws IOException;
}
