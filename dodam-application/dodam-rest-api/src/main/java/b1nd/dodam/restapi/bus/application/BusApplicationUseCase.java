package b1nd.dodam.restapi.bus.application;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.entity.BusApplication;
import b1nd.dodam.domain.rds.bus.entity.BusTime;
import b1nd.dodam.domain.rds.bus.enumeration.BusApplicationStatus;
import b1nd.dodam.domain.rds.bus.exception.BusAlreadyAppliedException;
import b1nd.dodam.domain.rds.bus.repository.BusApplicationRepository;
import b1nd.dodam.domain.rds.bus.repository.BusRepository;
import b1nd.dodam.domain.rds.bus.repository.BusTimeToBusRepository;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.bus.application.data.res.BusMemberRes;
import b1nd.dodam.restapi.bus.application.data.res.BusSeatRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BusApplicationUseCase {

    private final BusRepository busRepository;
    private final BusApplicationRepository busApplicationRepository;
    private final BusTimeToBusRepository busTimeToBusRepository;
    private final StudentRepository studentRepository;
    private final MemberAuthenticationHolder memberAuthenticationHolder;

    @Transactional(rollbackFor = Exception.class)
    public Response apply(int busId, int seatNumber) {
        isBusTimeAble(busId);
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        checkIfTheApplicationExists(student);
        Bus bus = adjustApplicationCount(busId, true);
        busApplicationRepository.save(
                BusApplication.builder()
                        .bus(bus)
                        .student(student)
                        .seatNumber(seatNumber)
                        .status(BusApplicationStatus.NOT_BOARDING)
                        .build()
        );
        return Response.created("버스 신청 성공");
    }

    private void isBusTimeAble(int id){
        BusTime busTime = busTimeToBusRepository.getByBusId(id).getBusTime();
        busTime.checkIfTheBusIsAvailable();
    }

    private Bus adjustApplicationCount(int busId, boolean increment) {
        Bus bus = busRepository.getByIdForUpdate(busId);
        if (increment) bus.increaseApplyCount();
        else bus.decreaseApplyCount();
        return bus;
    }

    public ResponseData<BusSeatRes> getSeatNumbers(int busId) {
        Bus bus = busRepository.getById(busId);
        List<BusApplication> busApplications = busApplicationRepository.findByBusAndStatusNot(bus, BusApplicationStatus.EXPIRED);
        return ResponseData.ok("버스 좌석 번호 조회 성공", BusSeatRes.of(busApplications));
    }

    private void checkIfTheApplicationExists(Student student) {
        if(busApplicationRepository.existsByStudentAndBus_LeaveAtAfterAndBus_LeaveTimeAfter(student, ZonedDateTimeUtil.nowToLocalDate(), ZonedDateTimeUtil.nowToLocalTime())) {
            throw new BusAlreadyAppliedException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response modify(int newBusId, int seat) {
        isBusTimeAble(newBusId);
        BusApplication application = getMy();
        adjustApplicationCount(application.getBus().getId(), false);
        application.updateBus(adjustApplicationCount(newBusId, true), seat);
        return Response.noContent("버스 신청 수정 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response cancel() {
        BusApplication application = getMy();
        adjustApplicationCount(application.getBus().getId(), false);
        busApplicationRepository.delete(application);
        return Response.noContent("버스 신청 취소 성공");
    }

    public ResponseData<List<BusMemberRes>> getBusStudent(int id, BusApplicationStatus status){
        return ResponseData.ok("버스 이용 현황 조회 성공",
                busApplicationRepository.findByBus_IdAndStatus(id, status)
                        .stream()
                        .map(BusMemberRes::createFromBusMember)
                        .toList()
        );
    }

    private BusApplication getMy() {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        return busApplicationRepository.getValidBusApplication(student, ZonedDateTimeUtil.nowToLocalDate(), ZonedDateTimeUtil.nowToLocalTime());
    }

}