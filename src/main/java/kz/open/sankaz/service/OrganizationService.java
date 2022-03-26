package kz.open.sankaz.service;

import kz.open.sankaz.model.Organization;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.pojo.dto.PageDto;
import kz.open.sankaz.pojo.filter.OrganizationCreateFilter;
import kz.open.sankaz.pojo.filter.OrganizationEditFilter;
import kz.open.sankaz.pojo.filter.OrganizationFilterFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OrganizationService extends CommonService<Organization> {
    Organization getOrganizationByTelNumber(String telNumber);
    Organization getOrganizationByUser(SecUser user);

    Organization getOrganizationByIban(String iban);

    Organization getOrganizationByIin(String iin);

    List<Organization> getAllByConfirmationStatuses(OrganizationFilterFilter filter);

    void approveOrganizationData(Long orgId);
    void rejectOrganizationData(Long orgId, String rejectMessage);

    void finishProfile(Long orgId, OrganizationEditFilter filter);

    void uploadPicture(Long orgId, MultipartFile pic) throws IOException;
    void uploadPicture(Long orgId, MultipartFile[] pic) throws IOException;

    Organization createOrg(OrganizationCreateFilter filter);

    Organization editOrg(Long orgId, OrganizationCreateFilter filter);

    boolean checkIfOwnOrg(Long orgId);

    PageDto getAllFilters(String name, String address, String companyCategoryCode, String confirmationStatus, int page, int size);

    List<SysFile> addPics(Long orgId, MultipartFile[] pics) throws IOException;

    void deletePics(Long orgId, Long picId);
}
