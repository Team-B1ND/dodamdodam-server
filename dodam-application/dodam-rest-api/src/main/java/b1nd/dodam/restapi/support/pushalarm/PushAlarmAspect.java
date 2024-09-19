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

    @Around("@annotation(pushAlarmEvent)")
    public Object handlePushAlarmEvent(ProceedingJoinPoint joinPoint, PushAlarmEvent pushAlarmEvent) {

        Object result = proceedJoinPointSafely(joinPoint);

        Long id = getIdFromArgs(joinPoint.getArgs());
        String rejectReason = getRejectReasonFromArgs(joinPoint.getArgs());
        String target = pushAlarmEvent.target();
        ApprovalStatus status = pushAlarmEvent.status();

        Student student = getStudentByTargetAndId(target, id);

        sendPushAlarm(student, target, rejectReason, status);

        return result;
    }

    private Object proceedJoinPointSafely(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new InternalServerException();
        }
    }

    private Long getIdFromArgs(Object[] args) {
        if (args[0] instanceof Long id) {
            return id;
        } else {
            throw new InternalServerException();
        }
    }

    private String getRejectReasonFromArgs(Object[] args) {
        if (args.length > 1 && args[1] instanceof Optional<?> optionalArg) {
            return (String) optionalArg.orElse(null);
        }
        return null;
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
        String pushToken = student.getMember().getPushToken();
        ApprovalAlarmEvent alarmEvent = ApprovalAlarmUtil.createAlarmEvent(pushToken, target, rejectReason, status);
        eventPublisher.publishEvent(alarmEvent);
    }
}