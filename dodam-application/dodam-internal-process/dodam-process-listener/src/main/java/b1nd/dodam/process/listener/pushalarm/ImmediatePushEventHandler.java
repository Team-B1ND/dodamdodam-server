
package b1nd.dodam.process.listener.pushalarm;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@RequiredArgsConstructor
public class ImmediatePushEventHandler {

    private final FcmProcessor processor;

    @Async
    @EventListener
    public void handleImmediateFcm(FcmEvent event) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            processor.process(event);
        }
    }
}