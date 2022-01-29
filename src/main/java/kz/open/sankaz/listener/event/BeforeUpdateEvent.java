package kz.open.sankaz.listener.event;

import org.springframework.context.ApplicationEvent;

public class BeforeUpdateEvent extends ApplicationEvent {
    public BeforeUpdateEvent(Object source) {
        super(source);
    }
}
