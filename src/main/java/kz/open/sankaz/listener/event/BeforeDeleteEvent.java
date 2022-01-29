package kz.open.sankaz.listener.event;

import org.springframework.context.ApplicationEvent;

public class BeforeDeleteEvent extends ApplicationEvent {
    public BeforeDeleteEvent(Object source) {
        super(source);
    }
}
