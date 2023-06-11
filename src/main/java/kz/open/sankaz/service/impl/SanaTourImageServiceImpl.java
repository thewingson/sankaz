package kz.open.sankaz.service.impl;

import kz.open.sankaz.image.SanaTourImage;
import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.RoomClassDic;
import kz.open.sankaz.model.San;
import kz.open.sankaz.pojo.dto.PageDto;
import kz.open.sankaz.pojo.filter.RoomCreateFilter;
import kz.open.sankaz.pojo.filter.SanaTourImageDTO;
import kz.open.sankaz.repo.CommonRepo;
import kz.open.sankaz.repo.RoomRepo;
import kz.open.sankaz.repo.SanaTourImageRepo;
import kz.open.sankaz.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@Transactional
public class SanaTourImageServiceImpl  implements SanaTourImageService{

    @Autowired
    SanaTourImageRepo repo;

    @Override
    public List<SanaTourImage> getAllByIdIn(List<Long> ids) {
        return null;
    }

    @Override
    public SanaTourImage getOne(Long id) {
        return null;
    }

    @Override
    public SanaTourImage getOne(Long id, Map<String, Object> params) {
        return null;
    }

    @Override
    public Page<SanaTourImage> getAll(int page, int size) {
        return null;
    }

    @Override
    public List<SanaTourImage> getAll(Map<String, Object> params) {
        return null;
    }

    @Override
    public SanaTourImage addOne(SanaTourImage entity) {
       SanaTourImage result= repo.save(entity);
       return result;
    }

    @Override
    public SanaTourImage editOneById(SanaTourImage entity) {
        return null;
    }

    @Override
    public List<SanaTourImage> saveAll(List<SanaTourImage> entities) {

        List<SanaTourImage> result= repo.saveAll(entities);
        return result;
    }

    @Override
    public void deleteOneById(Long id) throws SQLException {
        repo.deleteById(id);
    }

    @Override
    public void deleteList(List<SanaTourImage> list) {

    }

    @Override
    public void deleteOneByIdSoft(Long id) {

    }

    @Override
    public PageDto getAllPage(int page, int size) {
        return null;
    }

    @Override
    public List<SanaTourImage> getAll() {
        return null;
    }


}
