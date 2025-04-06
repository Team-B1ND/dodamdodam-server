package b1nd.dodam.domain.rds.support.event.listener;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
public class EntitySyncListener<T> {

    private static ApplicationEventPublisher eventPublisher;

    @PostUpdate
    public void onEntityChanged(T entity) {
        if (eventPublisher != null) {
            eventPublisher.publishEvent(entity);
        }
    }
}
