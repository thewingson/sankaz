package kz.open.sankaz.listener;

import kz.open.sankaz.listener.event.AfterCreateEvent;
import org.springframework.context.event.EventListener;

public interface AfterCreateListener {
    @EventListener
    void afterCreate(AfterCreateEvent event);
}
