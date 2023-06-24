package kz.open.sankaz.service;

import kz.open.sankaz.model.SanAdditional;
import kz.open.sankaz.model.SanAdditionalDic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SanAdditionalDicService")
public interface SanAdditionalService extends CommonService<SanAdditional> {



        List<SanAdditional> getAll();
        SanAdditional save(SanAdditional sanAdditional);
        List<SanAdditional> findAllByIds(List<Long> ids);
}
