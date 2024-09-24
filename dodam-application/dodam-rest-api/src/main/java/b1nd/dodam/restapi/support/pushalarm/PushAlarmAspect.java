package b1nd.dodam.restapi.support.pushalarm;

import b1nd.dodam.core.exception.global.InternalServerException;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.service.NightStudyService;
import b1nd.dodam.domain.rds.outgoing.service.OutGoingService;
import b1nd.dodam.domain.rds.outsleeping.service.OutSleepingService;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class PushAlarmAspect {
    private final ApplicationEventPublisher eventPublisher;
    private final NightStudyService nightStudyService;
    private final OutGoingService outGoingService;
    private final OutSleepingService outSleepingService;

    @AfterReturning(pointcut = "@annotation(pushAlarmEvent)", returning = "result")
    public void handlePushAlarmEvent(Object result, PushAlarmEvent pushAlarmEvent) {
        String target = pushAlarmEvent.target();
        sendPushAlarm(
                getStudentByTargetAndId(target, getIdFromArgs(result)),
                target,
                getRejectReasonFromArgs(result),
                pushAlarmEvent.status()
        );
    }

    private Long getIdFromArgs(Object result) {
        if (result instanceof Long id)
            return id;
        else throw new InternalServerException();
    }

    private String getRejectReasonFromArgs(Object result) {
        if (result instanceof Optional<?> optionalArg)
            return (String) optionalArg.orElse(null);
        else throw new InternalServerException();
    }

    private Student getStudentByTargetAndId(String target, Long id) {
        return switch (target) {
            case "외출" -> outGoingService.getById(id).getStudent();
            case "외박" -> outSleepingService.getById(id).getStudent();
            case "심야자습" -> nightStudyService.getBy(id).getStudent();
            default -> throw new InternalServerException();
        };
    }

    private void sendPushAlarm(Student student, String target, String rejectReason, ApprovalStatus status) {
        eventPublisher.publishEvent(ApprovalAlarmUtil.createAlarmEvent(
                student.getMember().getPushToken(), target, rejectReason, status));
    }
}