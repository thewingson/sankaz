package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.TelNumber;
import kz.open.sankaz.repo.TelNumberRepo;
import kz.open.sankaz.service.TelNumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class TelNumberServiceImpl extends AbstractService<TelNumber, TelNumberRepo> implements TelNumberService {

    private final TelNumberRepo telNumberRepo;


    public TelNumberServiceImpl(TelNumberRepo telNumberRepo) {
        super(telNumberRepo);
        this.telNumberRepo = telNumberRepo;
    }

    @Override
    protected Class getCurrentClass() {
        return TelNumber.class;
    }

    @Override
    protected Class getServiceClass(){
        return this.getClass();
    }
}
