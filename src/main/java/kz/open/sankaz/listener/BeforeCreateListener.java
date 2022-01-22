package kz.open.sankaz.listener;

import kz.open.sankaz.listener.event.CreateEvent;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public interface BeforeCreateListener<E extends CreateEvent>  {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void onBeforeCreate(E event);
}
