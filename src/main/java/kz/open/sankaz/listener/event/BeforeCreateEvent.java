package kz.open.sankaz.listener.event;

import org.springframework.context.ApplicationEvent;

public class BeforeCreateEvent extends ApplicationEvent {
    public BeforeCreateEvent(Object source) {
        super(source);
    }
}
