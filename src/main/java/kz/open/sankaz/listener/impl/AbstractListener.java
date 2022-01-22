package kz.open.sankaz.listener.impl;

import kz.open.sankaz.listener.*;
import kz.open.sankaz.listener.event.CreateEvent;
import kz.open.sankaz.listener.event.DeleteEvent;
import kz.open.sankaz.listener.event.UpdateEvent;
import kz.open.sankaz.model.AbstractEntity;
import kz.open.sankaz.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Slf4j
public abstract class AbstractListener implements BeforeCreateListener<CreateEvent>,
        AfterCreateListener<CreateEvent>,
        BeforeDeleteListener<DeleteEvent>,
        AfterDeleteListener<DeleteEvent>,
        BeforeUpdateListener<UpdateEvent>,
        AfterUpdateListener<UpdateEvent> {

    @Autowired
    private AuthService authService;

    @Override
    public void onAfterCreate(CreateEvent event) {
        log.info("LISTENER -> AbstractListener.onAfterCreate()");
    }

    @Override
    public void onAfterDelete(DeleteEvent event) {
        log.info("LISTENER -> AbstractListener.onAfterDelete()");
    }

    @Override
    public void onAfterUpdate(UpdateEvent event) {
        log.info("LISTENER -> AbstractListener.onAfterUpdate()");
    }

    @Override
    public void onBeforeCreate(CreateEvent event) {
        log.info("LISTENER -> AbstractListener.onBeforeCreate()");
        AbstractEntity entity = (AbstractEntity) event.getSource();
        entity.setCreatedBy(authService.getCurrentUser().getUsername());
    }

    @Override
    public void onBeforeDelete(DeleteEvent event) {
        log.info("LISTENER -> AbstractListener.onBeforeDelete()");
        AbstractEntity entity = (AbstractEntity) event.getSource();
        entity.setDeleteTs(LocalDateTime.now());
        entity.setDeletedBy(authService.getCurrentUser().getUsername());
    }

    @Override
    public void onBeforeUpdate(UpdateEvent event) {
        log.info("LISTENER -> AbstractListener.onBeforeUpdate()");
        AbstractEntity entity = (AbstractEntity) event.getSource();
        entity.setUpdateTs(LocalDateTime.now());
        entity.setUpdatedBy(authService.getCurrentUser().getUsername());
    }
}
