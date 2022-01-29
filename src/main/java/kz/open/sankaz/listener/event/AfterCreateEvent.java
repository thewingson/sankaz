package kz.open.sankaz.listener.event;

import org.springframework.context.ApplicationEvent;

public class AfterCreateEvent extends ApplicationEvent {
    public AfterCreateEvent(Object source) {
        super(source);
    }
}
