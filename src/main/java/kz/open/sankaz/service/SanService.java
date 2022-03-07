package kz.open.sankaz.service;

import kz.open.sankaz.model.Review;
import kz.open.sankaz.model.San;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.pojo.dto.SanForMainDto;
import kz.open.sankaz.pojo.filter.ReviewCreateFilter;
import kz.open.sankaz.pojo.filter.SanCreateFilter;
import kz.open.sankaz.pojo.filter.SanForMainFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SanService extends CommonService<San> {
    San createSan(SanCreateFilter filter);

    San updateOneDto(Long id, SanCreateFilter filter);

    San changeSanType(Long id, Long sanTypeId);

    San addTelNumbers(Long id, String[] telNumbers);

    void deleteTelNumbers(Long sanId, String[] telNumbers);

    Review addReview(Long sanId, ReviewCreateFilter filter);

    List<SanForMainDto> getAllForMain(SanForMainFilter filter, int page, int size);

    void addGeo(Long sanId, Double longitude, Double latitude);

    List<SysFile> addPics(Long sanId, MultipartFile[] pics) throws IOException;

    void deletePics(Long sanId, Long[] picIds);

    List<San> getAllOwn();

    boolean checkIfOwnOrg(Long orgId);
    boolean checkIfOwnSan(Long sanId);
}
