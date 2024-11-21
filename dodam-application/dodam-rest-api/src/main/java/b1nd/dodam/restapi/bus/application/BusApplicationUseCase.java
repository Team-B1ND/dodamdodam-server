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

        return busApplication
                .map(application -> handleExistingApplication(busId, application))
                .orElseGet(() -> applyForNewBus(busId, student));
    }

    private Response handleExistingApplication(int busId, BusApplication currentApplication) {
        return (currentApplication.getBus().getId() == busId)
                ? cancelCurrentApplication(currentApplication)
                : updateToNewBus(busId, currentApplication);
    }

    private Response applyForNewBus(int busId, Student student) {
        Bus bus = adjustApplicationCount(busId, true);
        busApplicationRepository.save(
                BusApplication.builder()
                        .bus(bus)
                        .student(student)
                        .build()
        );
        return Response.created("버스 신청 성공");
    }

    private Response updateToNewBus(int busId, BusApplication currentApplication) {
        cancelCurrentApplication(currentApplication);
        Bus newBus = adjustApplicationCount(busId, true);
        currentApplication.updateBus(newBus);
        return Response.noContent("버스 신청 수정 성공");
    }

    private Response cancelCurrentApplication(BusApplication currentApplication) {
        adjustApplicationCount(currentApplication.getBus().getId(), false);
        busApplicationRepository.delete(currentApplication);
        return Response.noContent("버스 신청 취소 성공");
    }

    private Bus adjustApplicationCount(int busId, boolean increment) {
        Bus bus = busRepository.getByIdForUpdate(busId);
        if (increment) bus.increaseApplyCount();
        else bus.decreaseApplyCount();
        return bus;
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