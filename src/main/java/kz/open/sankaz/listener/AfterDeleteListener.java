package kz.open.sankaz.listener;

import kz.open.sankaz.listener.event.AfterDeleteEvent;
import org.springframework.context.event.EventListener;

public interface AfterDeleteListener {
    @EventListener
    void afterDelete(AfterDeleteEvent event);
}
