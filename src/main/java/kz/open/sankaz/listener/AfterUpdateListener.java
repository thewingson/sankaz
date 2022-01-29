package kz.open.sankaz.listener;

import kz.open.sankaz.listener.event.AfterUpdateEvent;
import org.springframework.context.event.EventListener;

public interface AfterUpdateListener {
    @EventListener
    void afterUpdate(AfterUpdateEvent event);
}
