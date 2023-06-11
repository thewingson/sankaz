package kz.open.sankaz.service;

import kz.open.sankaz.model.Room;
import kz.open.sankaz.pojo.filter.RoomCreateFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface RoomService extends CommonService<Room> {
    Room addOne(RoomCreateFilter filter);
    Room editOneById(Long roomId, RoomCreateFilter filter);

    List<Room> getAllByClass(Long classId);

    List<Room> getAllByDate(Long sanId, LocalDateTime startDate, LocalDateTime endDate);
}
