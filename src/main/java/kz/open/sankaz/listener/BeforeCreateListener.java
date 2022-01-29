package kz.open.sankaz.listener;

import kz.open.sankaz.listener.event.BeforeCreateEvent;
import org.springframework.context.event.EventListener;

public interface BeforeCreateListener {
    @EventListener
    void beforeCreate(BeforeCreateEvent event);
}
