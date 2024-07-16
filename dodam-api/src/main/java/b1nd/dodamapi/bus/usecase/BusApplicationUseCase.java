package b1nd.dodamapi.bus.usecase;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamcore.bus.application.BusApplicationService;
import b1nd.dodamcore.bus.application.BusService;
import b1nd.dodamcore.bus.domain.entity.Bus;
import b1nd.dodamcore.bus.domain.entity.BusMember;
import b1nd.dodamcore.bus.domain.exception.BusAlreadyApplyException;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.domain.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BusApplicationUseCase {

    private final BusService busService;
    private final BusApplicationService busApplicationService;
    private final MemberService memberService;

    public Response apply(int busId) {
        Student student = memberService.getStudentFromSession();
        if(busApplicationService.hasMy(student)) {
            throw new BusAlreadyApplyException();
        }
        busApplicationService.save(BusMember.builder()
                .bus(increaseBusApplicationCount(busId))
                .student(student)
                .build()
        );
        return Response.created("버스 신청 성공");
    }

    public Response modify(int newBusId) {
        BusMember application = busApplicationService.getMy(memberService.getStudentFromSession());
        decreaseBusApplicationCount(application.getBus().getId());
        application.modifyBus(increaseBusApplicationCount(newBusId));
        return Response.noContent("버스 신청 수정 성공");
    }

    public Response cancel() {
        BusMember application = busApplicationService.getMy(memberService.getStudentFromSession());
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
