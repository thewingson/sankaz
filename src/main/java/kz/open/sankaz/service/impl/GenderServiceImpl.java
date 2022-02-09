package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.Gender;
import kz.open.sankaz.repo.dictionary.GenderRepo;
import kz.open.sankaz.service.GenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
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
}
