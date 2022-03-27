package kz.open.sankaz.service;

import kz.open.sankaz.model.RoomClassDic;
import kz.open.sankaz.pojo.filter.RoomClassDicFilter;

import java.util.List;

public interface RoomClassDicService extends CommonService<RoomClassDic> {
    RoomClassDic addOne(RoomClassDicFilter filter);
    RoomClassDic editOne(Long id, RoomClassDicFilter filter);
    List<RoomClassDic> getBySan(Long sanId);
    RoomClassDic getOne(Long dicId, Long sanId);
}
