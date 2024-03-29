package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.listener.event.*;
import kz.open.sankaz.model.BaseEntity;
import kz.open.sankaz.pojo.dto.PageDto;
import kz.open.sankaz.repo.CommonRepo;
import kz.open.sankaz.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
public abstract class AbstractService<E extends BaseEntity, R extends CommonRepo<E>> implements CommonService<E> {

    protected final R repo;
    @Autowired
    protected ApplicationEventPublisher applicationEventPublisher;

    protected <T extends CommonRepo<E>> AbstractService(T repo) {
        this.repo = (R) repo;
    }

    @Override
    public List<E> getAllByIdIn(List<Long> ids) {
        return repo.getAllByIdIn(ids);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public E getOne(Long id) {
        Optional<E> entityById = repo.findById(id);
        if(!entityById.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("ID", id);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return entityById.get();
    }

    @Override
    public E getOne(Long id, Map<String, Object> params) {
        Optional<E> entityById;
        if (params.containsKey("deleted") && (Boolean) params.get("deleted")) {
            entityById = repo.findById(id);
        } else {
            entityById = repo.findById(id);
        }
        if(!entityById.isPresent()){
            Map<String, Object> errorParams = new HashMap<>();
            errorParams.put("ID", id);
            throw new EntityNotFoundException(getCurrentClass(), errorParams);
        }
        return entityById.get();
    }

    @Override
    public List<E> getAll() {
        return repo.findAll();
    }

    @Override
    public Page<E> getAll(int page, int size) {
        return repo.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<E> getAll(Map<String, Object> params) {
        if (params.containsKey("deleted") && (Boolean) params.get("deleted")) {
            return repo.findAll();
        } else {
            return repo.findAll();
        }
    }

    @Override
    public E addOne(E entity) {
        entity = repo.save(entity);
        return entity;
    }

    @Override
    public E editOneById(E entity) {
        entity = repo.save(entity);
        return entity;
    }

    @Override
    public void deleteOneById(Long id) throws SQLException {
        E one = getOne(id);
        repo.delete(one);
    }

    @Override
    public void deleteList(List<E> list) {
        repo.deleteAll(list);
    }

    @Override
    public void deleteOneByIdSoft(Long id) {
        E one = getOne(id);
        repo.save(one);
    }

    @Override
    public List<E> saveAll(List<E> entities) {
        return repo.saveAll(entities);
    }

    @Override
    public PageDto getAllPage(int page, int size) {
        Page<E> pages = getAll(page, size);
        PageDto dto = new PageDto();
        dto.setTotal(pages.getTotalElements());
        dto.setContent(pages.getContent());
        dto.setPageable(pages.getPageable());
        return dto;
    }

    protected abstract Class getCurrentClass();

    protected Class getServiceClass(){
        return this.getClass();
    }

    protected BeforeCreateEvent getBeforeCreateEvent(E entity) { return new BeforeCreateEvent(entity); }

    protected BeforeDeleteEvent getBeforeDeleteEvent(E entity) {
        return new BeforeDeleteEvent(entity);
    }

    protected BeforeUpdateEvent getBeforeUpdateEvent(E entity) {
        return new BeforeUpdateEvent(entity);
    }

    protected AfterCreateEvent getAfterCreateEvent(E entity) { return new AfterCreateEvent(entity); }

    protected AfterDeleteEvent getAfterDeleteEvent(E entity) {
        return new AfterDeleteEvent(entity);
    }

    protected AfterUpdateEvent getAfterUpdateEvent(E entity) {
        return new AfterUpdateEvent(entity);
    }
}