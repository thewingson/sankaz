package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.model.AbstractDictionaryEntity;
import kz.open.sankaz.repo.dictionary.CommonDictionaryRepo;
import kz.open.sankaz.service.CommonDictionaryService;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
public abstract class AbstractDictionaryService<E extends AbstractDictionaryEntity, R extends CommonDictionaryRepo<E>>
        extends AbstractService<E, R>
        implements CommonDictionaryService<E> {

    protected <T extends CommonDictionaryRepo<E>> AbstractDictionaryService(T repo) {
        super(repo);
    }

    @Override
    public List<E> getAllByCodeIn(List<String> codes) {
        return repo.getAllByCodeIn(codes);
    }

    @Override
    public E getOneByCode(String code) {
        Optional<E> entityByCode = repo.getByCode(code);
        if(!entityByCode.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("CODE", code);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return entityByCode.get();
    }

}