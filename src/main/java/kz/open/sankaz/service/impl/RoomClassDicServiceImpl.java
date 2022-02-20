package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.CompanyCategory;
import kz.open.sankaz.model.RoomClassDic;
import kz.open.sankaz.model.San;
import kz.open.sankaz.pojo.filter.RoomClassDicFilter;
import kz.open.sankaz.repo.dictionary.RoomClassDicRepo;
import kz.open.sankaz.service.RoomClassDicService;
import kz.open.sankaz.service.SanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class RoomClassDicServiceImpl extends AbstractService<RoomClassDic, RoomClassDicRepo> implements RoomClassDicService {

    @Autowired
    private SanService sanService;


    public RoomClassDicServiceImpl(RoomClassDicRepo roomClassDicRepo) {
        super(roomClassDicRepo);
    }

    @Override
    protected Class getCurrentClass() {
        return RoomClassDic.class;
    }

    @Override
    protected Class getServiceClass(){
        return this.getClass();
    }

    @Override
    public RoomClassDic addOne(RoomClassDicFilter filter) {
        log.info(getServiceClass().getSimpleName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " Started");
        San san = sanService.getOne(filter.getSanId());

        RoomClassDic dictionary = new RoomClassDic();
        dictionary.setSan(san);
        dictionary.setCode(filter.getCode());
        dictionary.setName(filter.getName());
        dictionary.setDescription(filter.getDescription());
        dictionary.setNameKz(filter.getNameKz());
        dictionary.setDescriptionKz(filter.getDescriptionKz());
        return addOne(dictionary);
    }

    @Override
    public RoomClassDic editOne(Long id, RoomClassDicFilter filter) {
        log.info(getServiceClass().getSimpleName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " Started");
        RoomClassDic dictionary = getOne(id);
        dictionary.setSan(sanService.getOne(filter.getSanId()));

        dictionary.setCode(filter.getCode());
        dictionary.setName(filter.getName());
        dictionary.setDescription(filter.getDescription());
        dictionary.setNameKz(filter.getNameKz());
        dictionary.setDescriptionKz(filter.getDescriptionKz());
        return editOneById(dictionary);
    }
}
