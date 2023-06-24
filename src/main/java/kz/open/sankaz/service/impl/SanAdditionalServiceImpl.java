package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.SanAdditional;
import kz.open.sankaz.model.SanAdditionalDic;
import kz.open.sankaz.pojo.dto.PageDto;
import kz.open.sankaz.repo.SanAdditionalRepo;
import kz.open.sankaz.repo.dictionary.SanAdditionalDicRepo;
import kz.open.sankaz.service.SanAdditionalDicService;
import kz.open.sankaz.service.SanAdditionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Transactional
@Service("SanAdditionalService")
public class SanAdditionalServiceImpl implements SanAdditionalService {
    @Autowired
    SanAdditionalRepo repo;

    @Override
    public List<SanAdditional> getAllByIdIn(List<Long> ids) {
        return null;
    }

    @Override
    public SanAdditional getOne(Long id, Map<String, Object> params) {
        return null;
    }

    @Override
    public Page<SanAdditional> getAll(int page, int size) {
        return null;
    }

    @Override
    public List<SanAdditional> getAll(Map<String, Object> params) {
        return null;
    }

    @Override
    public SanAdditional addOne(SanAdditional entity) {
        return null;
    }




    @Override
    public SanAdditional editOneById(SanAdditional entity) {
        return null;
    }

    @Override
    public List<SanAdditional> saveAll(List<SanAdditional> entities) {
        return null;
    }

    @Override
    public void deleteOneById(Long id) throws SQLException {

    }

    @Override
    public void deleteList(List<SanAdditional> list) {

    }

    @Override
    public void deleteOneByIdSoft(Long id) {

    }

    @Override
    public PageDto getAllPage(int page, int size) {
        return null;
    }

    @Override
    public List<SanAdditional> getAll() {
        List<SanAdditional> result= repo.findAll();
        return  result;

    }

    @Override
    public SanAdditional getOne(Long id) {
        Optional<SanAdditional> result=repo.findById(id);
        return result.orElse(new SanAdditional());
    }

    @Override
    public SanAdditional save(SanAdditional sanAdditionalDic) {
        return  repo.save(sanAdditionalDic);
    }

    @Override
    public List<SanAdditional> findAllByIds(List<Long> ids) {
         List<SanAdditional> result=repo.getAllByIdIn(ids);
         return result;
    }
}
