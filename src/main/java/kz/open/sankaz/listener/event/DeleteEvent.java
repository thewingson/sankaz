package kz.open.sankaz.listener.event;

import org.springframework.context.ApplicationEvent;

public class DeleteEvent extends ApplicationEvent {
    public DeleteEvent(Object source) {
        super(source);
    }
}
