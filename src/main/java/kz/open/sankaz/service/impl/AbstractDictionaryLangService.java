package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.AbstractDictionaryLangEntity;
import kz.open.sankaz.repo.dictionary.CommonDictionaryLangRepo;
import kz.open.sankaz.repo.dictionary.CommonDictionaryRepo;
import kz.open.sankaz.service.CommonDictionaryLangService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractDictionaryLangService<E extends AbstractDictionaryLangEntity, R extends CommonDictionaryLangRepo<E>>
        extends AbstractDictionaryService<E, R>
        implements CommonDictionaryLangService<E> {

//    protected final Class<E> clazz;

    protected <T extends CommonDictionaryRepo<E>> AbstractDictionaryLangService(T repo) {
        super(repo);
//        this.clazz = clazz;
    }

//    public E updateOne(Long id, DictionaryLangFilter filter) {
//         E entity = clazz.newInstance();
//         getCurrentClass().newInstance();
//    }
//
//    public E addOne(DictionaryLangFilter filter) {
//        return null;
//    }

}