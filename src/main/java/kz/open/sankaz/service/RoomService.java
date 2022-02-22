package kz.open.sankaz.service;

import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.pojo.filter.RoomCreateFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RoomService extends CommonService<Room> {
    Room addOne(RoomCreateFilter filter);

    Room editOneById(Long roomId, RoomCreateFilter filter);

    List<SysFile> addPics(Long roomId, MultipartFile[] pics) throws IOException;

    void deletePics(Long roomId, Long[] picIds);

    boolean checkIfOwnRoom(Long roomId);

    List<Room> getAllByClass(Long classId);
}
