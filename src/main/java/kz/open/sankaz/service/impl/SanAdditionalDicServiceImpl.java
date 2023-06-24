package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.RoomAdditional;
import kz.open.sankaz.model.SanAdditionalDic;
import kz.open.sankaz.pojo.dto.PageDto;
import kz.open.sankaz.repo.RoomAdditionalRepo;
import kz.open.sankaz.repo.dictionary.SanAdditionalDicRepo;
import kz.open.sankaz.service.RoomAdditionalService;
import kz.open.sankaz.service.SanAdditionalDicService;
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
@Service("SanAdditionalDicService")
public class SanAdditionalDicServiceImpl implements SanAdditionalDicService {
    @Autowired
    SanAdditionalDicRepo repo;

    @Override
    public List<SanAdditionalDic> getAllByIdIn(List<Long> ids) {
        return null;
    }

    @Override
    public SanAdditionalDic getOne(Long id, Map<String, Object> params) {
        return null;
    }

    @Override
    public Page<SanAdditionalDic> getAll(int page, int size) {
        return null;
    }

    @Override
    public List<SanAdditionalDic> getAll(Map<String, Object> params) {
        return null;
    }

    @Override
    public SanAdditionalDic addOne(SanAdditionalDic entity) {
        return null;
    }

    @Override
    public SanAdditionalDic editOneById(SanAdditionalDic entity) {
        return null;
    }

    @Override
    public List<SanAdditionalDic> saveAll(List<SanAdditionalDic> entities) {
        return null;
    }

    @Override
    public void deleteOneById(Long id) throws SQLException {

    }

    @Override
    public void deleteList(List<SanAdditionalDic> list) {

    }

    @Override
    public void deleteOneByIdSoft(Long id) {

    }

    @Override
    public PageDto getAllPage(int page, int size) {
        return null;
    }

    @Override
    public List<SanAdditionalDic> getAll() {
        List<SanAdditionalDic> result= repo.findAll();
        return  result;

    }

    @Override
    public SanAdditionalDic getOne(Long id) {
        Optional<SanAdditionalDic> result=repo.findById(id);
        return result.orElse(new SanAdditionalDic());
    }

    @Override
    public SanAdditionalDic save(SanAdditionalDic sanAdditionalDic) {
        return  repo.save(sanAdditionalDic);
    }

    @Override
    public List<SanAdditionalDic> findAllByIds(List<Long> ids) {
         List<SanAdditionalDic> result=repo.getAllByIdIn(ids);
         return result;
    }
}
