package b1nd.dodamapi.bus.usecase;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamcore.bus.application.BusApplicationService;
import b1nd.dodamcore.bus.application.BusService;
import b1nd.dodamcore.bus.domain.entity.Bus;
import b1nd.dodamcore.bus.domain.entity.BusMember;
import b1nd.dodamcore.bus.domain.exception.BusAlreadyApplyException;
import b1nd.dodamcore.bus.domain.exception.BusMemberNotFoundException;
import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.domain.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.function.Consumer;


@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BusApplicationUseCase {

    private final BusService busService;
    private final BusApplicationService busApplicationService;
    private final MemberService memberService;

    public Response apply(int busId) {
        Bus bus = busService.getByIdForUpdate(busId);
        Student student = memberService.getStudentFromSession();
        if(busApplicationService.hasValidApplication(student, ZonedDateTimeUtil.nowToLocalDateTime())) {
            throw new BusAlreadyApplyException();
        }
        busApplicationService.save(
                BusMember.builder()
                        .bus(bus)
                        .student(student)
                        .build()
        );
        return Response.created("버스 신청 성공");
    }

    public Response modify(int busId) {
        Bus newBus = busService.getByIdForUpdate(busId);
        Student student = memberService.getStudentFromSession();
        findValidApplication(
                student,
                ZonedDateTimeUtil.nowToLocalDateTime(),
                (busMember) -> {
                    Bus oldBus = busService.getByIdForUpdate(busMember.getBus().getId());
                    oldBus.decreaseApplyCount();
                    busMember.modifyBus(newBus);
                    newBus.increaseApplyCount();
                }
        );
        return Response.noContent("버스 신청 수정 성공");
    }

    public Response cancel(int busId) {
        Student student = memberService.getStudentFromSession();
        findValidApplication(
                student,
                ZonedDateTimeUtil.nowToLocalDateTime(),
                (busMember) -> {
                    Bus bus = busService.getByIdForUpdate(busId);
                    bus.decreaseApplyCount();
                    busApplicationService.delete(busMember);
                }
        );
        return Response.noContent("버스 신청 취소 성공");
    }

    private void findValidApplication(Student student, LocalDateTime now, Consumer<? super BusMember> action) {
        busApplicationService.findValidApplication(student, now)
                .ifPresentOrElse(
                        action,
                        () -> {
                            throw new BusMemberNotFoundException();
                        }
                );
    }

}
