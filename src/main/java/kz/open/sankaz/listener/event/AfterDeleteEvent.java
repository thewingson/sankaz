package kz.open.sankaz.listener.event;

import org.springframework.context.ApplicationEvent;

public class AfterDeleteEvent extends ApplicationEvent {
    public AfterDeleteEvent(Object source) {
        super(source);
    }
}
