package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.Gender;
import kz.open.sankaz.pojo.filter.DictionaryLangFilter;
import kz.open.sankaz.repo.dictionary.GenderRepo;
import kz.open.sankaz.service.GenderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GenderServiceImpl extends AbstractDictionaryLangService<Gender, GenderRepo> implements GenderService {

    private final GenderRepo genderRepo;


    public GenderServiceImpl(GenderRepo genderRepo) {
        super(genderRepo);
        this.genderRepo = genderRepo;
    }

    @Override
    protected Class getCurrentClass() {
        return Gender.class;
    }

    @Override
    protected Class getServiceClass(){
        return this.getClass();
    }

    @Override
    public Gender updateOne(Long id, DictionaryLangFilter filter) {
        Gender gender = getOne(id);
        gender.setCode(filter.getCode());
        gender.setName(filter.getName());
        gender.setDescription(filter.getDescription());
        gender.setNameKz(filter.getNameKz());
        gender.setDescriptionKz(filter.getDescriptionKz());
        return addOne(gender);
    }

    @Override
    public Gender addOne(DictionaryLangFilter filter) {
        Gender gender = new Gender();
        gender.setCode(filter.getCode());
        gender.setName(filter.getName());
        gender.setDescription(filter.getDescription());
        gender.setNameKz(filter.getNameKz());
        gender.setDescriptionKz(filter.getDescriptionKz());
        return addOne(gender);
    }
}
