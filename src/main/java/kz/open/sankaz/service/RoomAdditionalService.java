package kz.open.sankaz.service;

import kz.open.sankaz.model.RoomAdditional;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface RoomAdditionalService extends CommonService<RoomAdditional> {

        List<RoomAdditional> getRoomAdditionals();
}
