package kz.open.sankaz.listener;

import kz.open.sankaz.listener.event.BeforeUpdateEvent;
import org.springframework.context.event.EventListener;

public interface BeforeUpdateListener {
    @EventListener
    void beforeUpdate(BeforeUpdateEvent event);
}
