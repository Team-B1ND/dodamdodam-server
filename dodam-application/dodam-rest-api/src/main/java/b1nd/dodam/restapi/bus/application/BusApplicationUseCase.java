package b1nd.dodam.restapi.bus.application;

import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.entity.BusApplication;
import b1nd.dodam.domain.rds.bus.exception.BusAlreadyApplyException;
import b1nd.dodam.domain.rds.bus.service.BusApplicationService;
import b1nd.dodam.domain.rds.bus.service.BusService;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.service.MemberService;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.support.data.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BusApplicationUseCase {

    private final BusService busService;
    private final BusApplicationService busApplicationService;
    private final MemberAuthenticationHolder memberAuthenticationHolder;
    private final MemberService memberService;

    public Response apply(int busId) {
        Student student = memberService.getStudentBy(memberAuthenticationHolder.current());
        if(busApplicationService.hasMy(student)) {
            throw new BusAlreadyApplyException();
        }
        busApplicationService.save(BusApplication.builder()
                .bus(increaseBusApplicationCount(busId))
                .student(student)
                .build()
        );
        return Response.created("버스 신청 성공");
    }

    public Response modify(int newBusId) {
        BusApplication application = busApplicationService.getMy(memberService.getStudentBy(memberAuthenticationHolder.current()));
        decreaseBusApplicationCount(application.getBus().getId());
        application.updateBus(increaseBusApplicationCount(newBusId));
        return Response.noContent("버스 신청 수정 성공");
    }

    public Response cancel() {
        BusApplication application = busApplicationService.getMy(memberService.getStudentBy(memberAuthenticationHolder.current()));
        decreaseBusApplicationCount(application.getBus().getId());
        busApplicationService.delete(application);
        return Response.noContent("버스 신청 취소 성공");
    }

    private Bus increaseBusApplicationCount(int busId) {
        Bus bus = busService.getByIdForUpdate(busId);
        bus.increaseApplyCount();
        return bus;
    }

    private void decreaseBusApplicationCount(int busId) {
        Bus bus = busService.getByIdForUpdate(busId);
        bus.decreaseApplyCount();
    }

}
