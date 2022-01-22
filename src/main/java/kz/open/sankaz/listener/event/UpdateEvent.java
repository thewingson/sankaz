package kz.open.sankaz.listener.event;

import org.springframework.context.ApplicationEvent;

public class UpdateEvent extends ApplicationEvent {
    public UpdateEvent(Object source) {
        super(source);
    }
}
