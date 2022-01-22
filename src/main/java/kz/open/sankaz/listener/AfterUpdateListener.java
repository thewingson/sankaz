package kz.open.sankaz.listener;

import kz.open.sankaz.listener.event.UpdateEvent;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public interface AfterUpdateListener<E extends UpdateEvent>  {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void onAfterUpdate(E event);
}
