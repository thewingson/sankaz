package kz.open.sankaz.listener.impl;

import kz.open.sankaz.listener.*;
import kz.open.sankaz.listener.event.*;
import kz.open.sankaz.model.AbstractEntity;
import kz.open.sankaz.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public abstract class AbstractListener implements BeforeCreateListener,
        AfterCreateListener,
        BeforeDeleteListener,
        AfterDeleteListener,
        BeforeUpdateListener,
        AfterUpdateListener {

    @Autowired
    private AuthService authService;

    @Override
    public void beforeCreate(BeforeCreateEvent event) {
        log.info("LISTENER -> AbstractListener.onBeforeCreate()");
        AbstractEntity entity = (AbstractEntity) event.getSource();
        entity.setCreatedBy(authService.getCurrentUsername());
    }

    @Override
    public void afterCreate(AfterCreateEvent event) {
        log.info("LISTENER -> AbstractListener.onAfterCreate()");
    }

    @Override
    public void beforeDelete(BeforeDeleteEvent event) {
        log.info("LISTENER -> AbstractListener.onBeforeDelete()");
        AbstractEntity entity = (AbstractEntity) event.getSource();
        entity.setDeleteTs(LocalDateTime.now());
        entity.setDeletedBy(authService.getCurrentUsername());
    }

    @Override
    public void afterDelete(AfterDeleteEvent event) {
        log.info("LISTENER -> AbstractListener.onAfterDelete()");
    }

    @Override
    public void beforeUpdate(BeforeUpdateEvent event) {
        log.info("LISTENER -> AbstractListener.onBeforeUpdate()");
        AbstractEntity entity = (AbstractEntity) event.getSource();
        entity.setUpdateTs(LocalDateTime.now());
        entity.setUpdatedBy(authService.getCurrentUsername());
    }

    @Override
    public void afterUpdate(AfterUpdateEvent event) {
        log.info("LISTENER -> AbstractListener.onAfterUpdate()");
    }
}
