package kz.open.sankaz.listener;

import kz.open.sankaz.listener.event.DeleteEvent;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public interface AfterDeleteListener<E extends DeleteEvent>  {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void onAfterDelete(E event);
}
