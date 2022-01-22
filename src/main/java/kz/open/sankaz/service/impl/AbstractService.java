package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.model.BaseEntity;
import kz.open.sankaz.repo.CommonRepo;
import kz.open.sankaz.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import javax.transaction.Transactional;
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
    public List<E> getAll() {
        return repo.findAll();
    }

    @Override
    public E addOne(E entity) {
        ApplicationEvent createEvent = getCreateEvent(entity);
        if(createEvent != null){
            applicationEventPublisher.publishEvent(createEvent);
        }

        return repo.save(entity);
    }

    @Override
    public E editOneById(E entity) {
        ApplicationEvent updateEvent = getUpdateEvent(entity);
        if(updateEvent != null){
            applicationEventPublisher.publishEvent(updateEvent);
        }

        return repo.save(entity);
    }

    @Override
    public void deleteOneById(Long id) {
        E one = getOne(id);

        ApplicationEvent deleteEvent = getDeleteEvent(one);
        if(deleteEvent != null){
            applicationEventPublisher.publishEvent(deleteEvent);
        }

        repo.save(one);
    }

    protected abstract Class getCurrentClass();
    protected abstract ApplicationEvent getCreateEvent(E entity);
    protected abstract ApplicationEvent getDeleteEvent(E entity);
    protected abstract ApplicationEvent getUpdateEvent(E entity);
}