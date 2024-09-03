package b1nd.dodam.restapi.bus.application;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.entity.BusApplication;
import b1nd.dodam.domain.rds.bus.exception.BusAlreadyAppliedException;
import b1nd.dodam.domain.rds.bus.repository.BusApplicationRepository;
import b1nd.dodam.domain.rds.bus.repository.BusRepository;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.support.data.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BusApplicationUseCase {

    private final BusRepository busRepository;
    private final BusApplicationRepository busApplicationRepository;
    private final StudentRepository studentRepository;
    private final MemberAuthenticationHolder memberAuthenticationHolder;

    public Response apply(int busId) {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        checkIfTheApplicationExists(student);
        busApplicationRepository.save(BusApplication.builder()
                .bus(increaseApplicationCount(busId))
                .student(student)
                .build()
        );
        return Response.created("버스 신청 성공");
    }

    private void checkIfTheApplicationExists(Student student) {
        if(busApplicationRepository.existsByStudentAndBus_LeaveTimeAfter(student, ZonedDateTimeUtil.nowToLocalDateTime())) {
            throw new BusAlreadyAppliedException();
        }
    }

    public Response modify(int newBusId) {
        BusApplication application = getMy();
        decreaseApplicationCount(application.getBus().getId());
        application.updateBus(increaseApplicationCount(newBusId));
        return Response.noContent("버스 신청 수정 성공");
    }

    public Response cancel() {
        BusApplication application = getMy();
        decreaseApplicationCount(application.getBus().getId());
        busApplicationRepository.delete(application);
        return Response.noContent("버스 신청 취소 성공");
    }

    private BusApplication getMy() {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        return busApplicationRepository.getByStudentAndBus_LeaveTimeAfter(student, ZonedDateTimeUtil.nowToLocalDateTime());
    }

    private Bus increaseApplicationCount(int id) {
        Bus bus = busRepository.getByIdForUpdate(id);
        bus.increaseApplyCount();
        return bus;
    }

    private void decreaseApplicationCount(int id) {
        Bus bus = busRepository.getByIdForUpdate(id);
        bus.decreaseApplyCount();
    }

}
