package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.City;
import kz.open.sankaz.model.Gender;
import kz.open.sankaz.model.RoomType;
import kz.open.sankaz.repo.dictionary.CityRepo;
import kz.open.sankaz.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class CityServiceImpl extends AbstractDictionaryLangService<City, CityRepo> implements CityService {

    private final CityRepo cityRepo;


    public CityServiceImpl(CityRepo cityRepo) {
        super(cityRepo);
        this.cityRepo = cityRepo;
    }

    @Override
    protected Class getCurrentClass() {
        return City.class;
    }

    @Override
    protected Class getServiceClass(){
        return this.getClass();
    }
}
