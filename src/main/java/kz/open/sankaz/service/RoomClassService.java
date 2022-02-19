package kz.open.sankaz.service;

import kz.open.sankaz.model.RoomClass;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.pojo.filter.RoomClassCreateFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RoomClassService extends CommonService<RoomClass> {
    RoomClass addOne(RoomClassCreateFilter filter);

    RoomClass editOneById(Long classId, RoomClassCreateFilter filter);

    List<SysFile> addPics(Long classId, MultipartFile[] pics) throws IOException;

    void deletePics(Long classId, Long[] picIds);
}
