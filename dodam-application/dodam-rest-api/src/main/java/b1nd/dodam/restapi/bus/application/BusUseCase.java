package b1nd.dodam.restapi.bus.application;

import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.entity.BusApplicant;
import b1nd.dodam.domain.rds.bus.enumeration.BoardingType;
import b1nd.dodam.domain.rds.bus.service.BusApplicantService;
import b1nd.dodam.domain.rds.bus.service.BusService;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.bus.application.data.req.BusApplicantReq;
import b1nd.dodam.restapi.bus.application.data.req.BusReq;
import b1nd.dodam.restapi.bus.application.data.res.BusApplicantRes;
import b1nd.dodam.restapi.bus.application.data.res.BusDetailRes;
import b1nd.dodam.restapi.bus.application.data.res.BusRes;
import b1nd.dodam.restapi.bus.application.data.res.StudentWithBusApplicantRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BusUseCase {
    private final BusService busService;
    private final StudentRepository studentRepository;
    private final BusApplicantService busApplicantService;
    private final MemberAuthenticationHolder authenticationHolder;

    public Response createBusApplicant(BusApplicantReq req) {
        List<Student> student = studentRepository.getByIds(req.studentId());
        List<BusApplicant> applicants = student.stream().map(s ->
                BusApplicant.builder()
                    .bus(busService.getById(req.busId()))
                    .student(s)
                    .boardingType(BoardingType.BEFORE_BOARDING)
                    .build()
            ).toList();

        busApplicantService.saveAll(applicants);
        return Response.created("버스 탑승자 생성 성공");
    }

    public ResponseData<BusDetailRes> busDetail(long id) {
        Bus bus = busService.getById(id);
        List<BusApplicant> busApplicants = busApplicantService.getByBus(bus);
        return ResponseData.ok("버스 상세 조회 성공", BusDetailRes.of(bus.getId(), bus.getName(), busApplicants.stream().map(StudentWithBusApplicantRes::of).toList()));
    }

    public ResponseData<List<Integer>> getRequestedSeats(long id) {
        Bus bus = busService.getById(id);
        return ResponseData.ok("버스 좌석 조회 성공", busApplicantService.getByBus(bus).stream().map(BusApplicant::getSeat).toList());
    }

    public ResponseData<List<BusRes>> getBus() {
        return ResponseData.ok("버스 조회 성공", busService.getAll().stream().map(BusRes::of).toList());
    }

    public Response createBus(BusReq req) {
        busService.save(req.toEntity());
        return Response.created("버스 생성 성공");
    }

    public Response deleteBus(long id) {
        busApplicantService.deleteByBus(id);
        busService.deleteById(id);
        return Response.noContent("버스 삭제 성공");
    }

    public Response apply(int seat) {
        Student student = studentRepository.getByMember(authenticationHolder.current());
        BusApplicant applicant = busApplicantService.getByStudent(student);
        busApplicantService.existsBySeatsAndBus(applicant.getBus(), seat);
        applicant.updateSeat(seat);
        applicant.updateBoardingType(BoardingType.BEFORE_BOARDING);
        busApplicantService.save(applicant);
        return Response.created("버스 신청 성공");
    }

    public Response changeBoardingType(BoardingType boardingType) {
        Student student = studentRepository.getByMember(authenticationHolder.current());
        BusApplicant applicant = busApplicantService.getByStudent(student);
        applicant.updateBoardingType(boardingType);
        busApplicantService.save(applicant);
        return Response.ok("탑승정보 수정 성공");
    }

    public Response changeSeat(int seat) {
        Student student = studentRepository.getByMember(authenticationHolder.current());
        BusApplicant applicant = busApplicantService.getByStudent(student);
        busApplicantService.existsBySeatsAndBus(applicant.getBus(), seat);
        applicant.updateSeat(seat);
        busApplicantService.save(applicant);
        return Response.ok("버스 좌석 변경 성공");
    }

    public Response updateBus(long id, BusReq req) {
        Bus bus = busService.getById(id);
        bus.modifyName(req.name());
        busService.save(bus);
        return Response.ok("버스 정보 수정 성공");
    }

    public ResponseData<BusApplicantRes> my() {
        Student student = studentRepository.getByMember(authenticationHolder.current());
        BusApplicant applicant = busApplicantService.getByStudentOrNull(student);

        return ResponseData.ok("내 버스 신청 정보 조회 성공", BusApplicantRes.of(applicant));
    }
}
