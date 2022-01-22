package kz.open.sankaz.listener;

import kz.open.sankaz.listener.event.UpdateEvent;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public interface BeforeUpdateListener<E extends UpdateEvent>  {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void onBeforeUpdate(E event);
}
