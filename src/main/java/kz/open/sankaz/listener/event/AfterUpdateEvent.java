package kz.open.sankaz.listener.event;

import org.springframework.context.ApplicationEvent;

public class AfterUpdateEvent extends ApplicationEvent {
    public AfterUpdateEvent(Object source) {
        super(source);
    }
}
