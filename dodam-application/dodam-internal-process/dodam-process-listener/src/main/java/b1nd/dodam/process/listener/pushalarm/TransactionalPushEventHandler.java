package b1nd.dodam.process.listener.pushalarm;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TransactionalPushEventHandler {

    private final FcmProcessor processor;

    @Async
    @TransactionalEventListener
    public void handleTransactionalFcm(FcmEvent event) {
        processor.process(event);
    }
}