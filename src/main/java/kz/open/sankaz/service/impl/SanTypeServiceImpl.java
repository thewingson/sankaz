package kz.open.sankaz.service.impl;

import kz.open.sankaz.mapper.CategoryMapper;
import kz.open.sankaz.model.SanType;
import kz.open.sankaz.pojo.filter.DictionaryLangFilter;
import kz.open.sankaz.repo.dictionary.SanTypeRepo;
import kz.open.sankaz.service.SanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SanTypeServiceImpl extends AbstractDictionaryService<SanType, SanTypeRepo> implements SanTypeService {

    private final SanTypeRepo sanTypeRepo;

    @Autowired
    private CategoryMapper categoryMapper;


    public SanTypeServiceImpl(SanTypeRepo sanTypeRepo) {
        super(sanTypeRepo);
        this.sanTypeRepo = sanTypeRepo;
    }

    @Override
    public List<SanType> getAllByCodeIn(List<String> codes) {
        return sanTypeRepo.getAllByCodeIn(codes);
    }

    @Override
    protected Class getCurrentClass() {
        return SanType.class;
    }

    @Override
    protected Class getServiceClass(){
        return this.getClass();
    }

    @Override
    public SanType addOne(DictionaryLangFilter filter) {
        SanType sanType = new SanType();
        sanType.setCode(filter.getCode());
        sanType.setName(filter.getName());
        sanType.setDescription(filter.getDescription());
        sanType.setNameKz(filter.getNameKz());
        sanType.setDescriptionKz(filter.getDescriptionKz());
        return addOne(sanType);
    }

    @Override
    public SanType updateOne(Long id, DictionaryLangFilter filter) {
        SanType sanType = getOne(id);
        sanType.setCode(filter.getCode());
        sanType.setName(filter.getName());
        sanType.setDescription(filter.getDescription());
        sanType.setNameKz(filter.getNameKz());
        sanType.setDescriptionKz(filter.getDescriptionKz());
        return addOne(sanType);
    }
}
