package kz.open.sankaz.service;

import kz.open.sankaz.model.Review;
import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.San;
import kz.open.sankaz.pojo.dto.FileUrlDto;
import kz.open.sankaz.pojo.dto.SanCreateDto;
import kz.open.sankaz.pojo.dto.SanDto;
import kz.open.sankaz.pojo.dto.SanForMainDto;
import kz.open.sankaz.pojo.filter.ReviewCreateFilter;
import kz.open.sankaz.pojo.filter.RoomCreateFilter;
import kz.open.sankaz.pojo.filter.SanCreateFilter;
import kz.open.sankaz.pojo.filter.SanForMainFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SanService extends CommonService<San>, CommonDtoService<San, SanDto> {

    /***
     * for Entity
     */

    /***
     * for DTO
     */
    San createSan(SanCreateFilter filter);

    San addOneDto(SanCreateDto dto);
    San updateOneDto(Long id, SanCreateFilter filter);

    SanDto addSanTypes(Long id, Long[] sanTypes);

    void deleteSanTypes(Long sanId, Long[] sanTypes);

    SanDto addTelNumbers(Long id, String[] telNumbers);

    void deleteTelNumbers(Long sanId, String[] telNumbers);

    List<FileUrlDto> addPics(Long id, MultipartFile[] pics) throws IOException;

    void deletePics(Long sanId, Long[] pics);

    Review addReview(Long sanId, ReviewCreateFilter filter);

    Room addRoom(Long sanId, RoomCreateFilter filter);

    List<FileUrlDto> addRoomPics(Long roomId, MultipartFile[] pics) throws IOException;

    void deleteRoomPics(Long roomId, Long[] pics);

    List<SanForMainDto> getAllForMain(SanForMainFilter filter);
}
