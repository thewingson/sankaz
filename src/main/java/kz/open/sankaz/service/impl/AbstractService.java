package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.listener.event.*;
import kz.open.sankaz.model.BaseEntity;
import kz.open.sankaz.repo.CommonRepo;
import kz.open.sankaz.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
public abstract class AbstractService<E extends BaseEntity, R extends CommonRepo<E>> implements CommonService<E> {

    @Value("${application.file.upload.path.image}")
    protected String APPLICATION_UPLOAD_PATH_IMAGE;

    @Value("${application.file.download.path.image}")
    protected String APPLICATION_DOWNLOAD_PATH_IMAGE;

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
    public void deleteOneById(Long id) {
        E one = getOne(id);
        repo.delete(one);
    }

    @Override
    public void deleteOneByIdSoft(Long id) {
        E one = getOne(id);
        repo.save(one);
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