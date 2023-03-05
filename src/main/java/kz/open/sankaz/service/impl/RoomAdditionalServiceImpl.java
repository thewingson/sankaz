package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.*;
import kz.open.sankaz.pojo.dto.PageDto;
import kz.open.sankaz.pojo.filter.RoomCreateFilter;
import kz.open.sankaz.repo.CommonRepo;
import kz.open.sankaz.repo.RoomAdditionalRepo;
import kz.open.sankaz.repo.RoomRepo;
import kz.open.sankaz.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@Transactional
public class RoomAdditionalServiceImpl implements RoomAdditionalService {
    @Autowired
    RoomAdditionalRepo repo;

    @Override
    public List<RoomAdditional> getAllByIdIn(List<Long> ids) {
        return null;
    }

    @Override
    public RoomAdditional getOne(Long id) {
        return null;
    }

    @Override
    public RoomAdditional getOne(Long id, Map<String, Object> params) {
        return null;
    }

    @Override
    public List<RoomAdditional> getAll() {
        return repo.findAll();
    }

    @Override
    public Page<RoomAdditional> getAll(int page, int size) {
        return null;
    }

    @Override
    public List<RoomAdditional> getAll(Map<String, Object> params) {
        return null;
    }

    @Override
    public RoomAdditional addOne(RoomAdditional entity) {
        return null;
    }

    @Override
    public RoomAdditional editOneById(RoomAdditional entity) {
        return null;
    }

    @Override
    public List<RoomAdditional> saveAll(List<RoomAdditional> entities) {
        return null;
    }

    @Override
    public void deleteOneById(Long id) throws SQLException {

    }

    @Override
    public void deleteList(List<RoomAdditional> list) {

    }

    @Override
    public void deleteOneByIdSoft(Long id) {

    }

    @Override
    public PageDto getAllPage(int page, int size) {
        return null;
    }

    @Override
    public List<RoomAdditional> getRoomAdditionals() {
        return repo.findAll();
    }
}
