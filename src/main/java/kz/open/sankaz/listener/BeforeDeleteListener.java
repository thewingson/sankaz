package kz.open.sankaz.listener;

import kz.open.sankaz.listener.event.BeforeDeleteEvent;
import org.springframework.context.event.EventListener;

public interface BeforeDeleteListener {
    @EventListener
    void beforeDelete(BeforeDeleteEvent event);
}