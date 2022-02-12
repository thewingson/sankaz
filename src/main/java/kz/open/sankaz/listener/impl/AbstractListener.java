package kz.open.sankaz.listener.impl;

import kz.open.sankaz.listener.*;
import kz.open.sankaz.listener.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public abstract class AbstractListener implements BeforeCreateListener,
        AfterCreateListener,
        BeforeDeleteListener,
        AfterDeleteListener,
        BeforeUpdateListener,
        AfterUpdateListener {

    @Override
    public void beforeCreate(BeforeCreateEvent event) {
        log.info("LISTENER -> AbstractListener.onBeforeCreate()");
    }

    @Override
    public void afterCreate(AfterCreateEvent event) {
        log.info("LISTENER -> AbstractListener.onAfterCreate()");
    }

    @Override
    public void beforeDelete(BeforeDeleteEvent event) {
        log.info("LISTENER -> AbstractListener.onBeforeDelete()");
    }

    @Override
    public void afterDelete(AfterDeleteEvent event) {
        log.info("LISTENER -> AbstractListener.onAfterDelete()");
    }

    @Override
    public void beforeUpdate(BeforeUpdateEvent event) {
        log.info("LISTENER -> AbstractListener.onBeforeUpdate()");
    }

    @Override
    public void afterUpdate(AfterUpdateEvent event) {
        log.info("LISTENER -> AbstractListener.onAfterUpdate()");
    }
}
