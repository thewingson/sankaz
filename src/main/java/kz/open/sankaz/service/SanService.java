package kz.open.sankaz.service;

import kz.open.sankaz.model.Review;
import kz.open.sankaz.model.San;
import kz.open.sankaz.model.Stock;
import kz.open.sankaz.pojo.dto.SanForMainAdminDto;
import kz.open.sankaz.pojo.dto.SanForMainDto;
import kz.open.sankaz.pojo.filter.*;
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

    Review answerToReview(Long sanId, ReviewModerCreateFilter filter);

    List<SanForMainDto> getAllForMain(Long userId, SanForMainFilter filter, int page, int size);

    List<SanForMainAdminDto> getAllForMainAdmin(Long userId, SanForMainFilter filter, int page, int size);

    List<SanForMainDto> getFavs(Long userId, int page, int size);

    void addGeo(Long sanId, Double longitude, Double latitude);

    List<San> getAllOwn();

    boolean checkIfOwnOrg(Long orgId);
    boolean checkIfOwnSan(Long sanId);

    Stock addStock(Long sanId, StockCreateFilter filter);

    Stock editStock(Long stockId, StockCreateFilter filter);

    void deleteStock(Long stockId);

    void addFav(Long userId, Long sanId);

    void deleteFav(Long userId, Long sanId);
}
