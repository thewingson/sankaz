package kz.open.sankaz.listener;

import kz.open.sankaz.listener.event.CreateEvent;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public interface AfterCreateListener<E extends CreateEvent>  {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void onAfterCreate(E event);
}
