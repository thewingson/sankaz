package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.City;
import kz.open.sankaz.pojo.filter.DictionaryLangFilter;
import kz.open.sankaz.repo.dictionary.CityRepo;
import kz.open.sankaz.service.CityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

    @Override
    public City updateOne(Long id, DictionaryLangFilter filter) {
        City city = getOne(id);
        city.setCode(filter.getCode());
        city.setName(filter.getName());
        city.setDescription(filter.getDescription());
        city.setNameKz(filter.getNameKz());
        city.setDescriptionKz(filter.getDescriptionKz());
        return addOne(city);
    }

    @Override
    public City addOne(DictionaryLangFilter filter) {
        City city = new City();
        city.setCode(filter.getCode());
        city.setName(filter.getName());
        city.setDescription(filter.getDescription());
        city.setNameKz(filter.getNameKz());
        city.setDescriptionKz(filter.getDescriptionKz());
        return addOne(city);
    }
}
