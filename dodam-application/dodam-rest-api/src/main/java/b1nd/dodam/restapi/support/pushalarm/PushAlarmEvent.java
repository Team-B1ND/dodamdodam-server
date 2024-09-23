package b1nd.dodam.restapi.support.pushalarm;

import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PushAlarmEvent {
    String target();
    ApprovalStatus status();
}