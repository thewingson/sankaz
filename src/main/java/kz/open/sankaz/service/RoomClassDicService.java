package kz.open.sankaz.service;

import kz.open.sankaz.model.RoomClassDic;
import kz.open.sankaz.pojo.filter.RoomClassDicFilter;

public interface RoomClassDicService extends CommonService<RoomClassDic> {
    RoomClassDic addOne(RoomClassDicFilter filter);
    RoomClassDic editOne(Long id, RoomClassDicFilter filter);
}
