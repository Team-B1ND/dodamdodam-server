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

import java.util.Optional;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BusApplicationUseCase {

    private final BusRepository busRepository;
    private final BusApplicationRepository busApplicationRepository;
    private final StudentRepository studentRepository;
    private final MemberAuthenticationHolder memberAuthenticationHolder;

    public Response modifyStatus(int busId) {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        Optional<BusApplication> busApplication = busApplicationRepository.findByStudentAndBus_LeaveTimeAfter(student, ZonedDateTimeUtil.nowToLocalDateTime());
        if (busApplication.isEmpty()) {
            applyBus(busId, student);
            return Response.created("버스 신청 성공");
        }
        if (busApplication.get().getBus().getId() == busId){
            cancelBus(busId);
            return Response.noContent("버스 신청 취소 성공");
        }
        else {
            return modify(busId);
        }
    }

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

    private Bus applyBus(int id, Student student) {
        Bus bus = busRepository.getByIdForUpdate(id);
        bus.increaseApplyCount();
        BusApplication newApplication = BusApplication.builder()
                .bus(bus)
                .student(student)
                .build();
        busApplicationRepository.save(newApplication);
        return bus;
    }

    private Bus increaseApplicationCount(int id) {
        Bus bus = busRepository.getByIdForUpdate(id);
        bus.increaseApplyCount();
        return bus;
    }

    private void cancelBus(int id) {
        Bus bus = busRepository.getByIdForUpdate(id);
        BusApplication application = getMy();
        decreaseApplicationCount(application.getBus().getId());
        busApplicationRepository.delete(application);
        bus.decreaseApplyCount();
    }

    private void decreaseApplicationCount(int id) {
        Bus bus = busRepository.getByIdForUpdate(id);
        bus.decreaseApplyCount();
    }

}
