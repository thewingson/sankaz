package kz.open.sankaz.listener;

import kz.open.sankaz.listener.event.DeleteEvent;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public interface BeforeDeleteListener<E extends DeleteEvent>  {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void onBeforeDelete(E event);
}