package kz.open.sankaz.service;

import kz.open.sankaz.dto.*;
import kz.open.sankaz.model.San;
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
    San addOneDto(SanCreateDto dto);
    San updateOneDto(Long id, SanCreateDto dto);

    SanDto addSanTypes(Long id, Long[] sanTypes);

    void deleteSanTypes(Long sanId, Long[] sanTypes);

    SanDto addTelNumbers(Long id, String[] telNumbers);

    void deleteTelNumbers(Long sanId, String[] telNumbers);

    List<FileUrlDto> addPics(Long id, MultipartFile[] pics) throws IOException;

    void deletePics(Long sanId, Long[] pics);

    ReviewCreateDto addReview(Long sanId, ReviewCreateDto dto);

    RoomCreateDto addRoom(Long sanId, RoomCreateDto dto);

    List<FileUrlDto> addRoomPics(Long roomId, MultipartFile[] pics) throws IOException;

    void deleteRoomPics(Long roomId, Long[] pics);
}
