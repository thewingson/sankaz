package kz.open.sankaz.service;

import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.RoomAdditional;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.pojo.filter.RoomCreateFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
@Service
public interface RoomAdditionalService extends CommonService<RoomAdditional> {

        List<RoomAdditional> getRoomAdditionals();
}
