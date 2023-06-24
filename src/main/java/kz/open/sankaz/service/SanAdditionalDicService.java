package kz.open.sankaz.service;

import kz.open.sankaz.model.RoomAdditional;
import kz.open.sankaz.model.SanAdditionalDic;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("SanAdditionalDicService")
public interface SanAdditionalDicService extends CommonService<SanAdditionalDic> {

        List<SanAdditionalDic> getAll();
        SanAdditionalDic save(SanAdditionalDic sanAdditionalDic);
        List<SanAdditionalDic> findAllByIds(List<Long> ids);
}
