package kz.open.sankaz.listener.event;

import org.springframework.context.ApplicationEvent;

public class CreateEvent extends ApplicationEvent {
    public CreateEvent(Object source) {
        super(source);
    }
}
