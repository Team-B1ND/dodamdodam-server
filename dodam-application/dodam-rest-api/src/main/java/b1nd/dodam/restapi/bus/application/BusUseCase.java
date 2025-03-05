package b1nd.dodam.restapi.bus.application;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.entity.BusApplication;
import b1nd.dodam.domain.rds.bus.entity.BusPreset;
import b1nd.dodam.domain.rds.bus.enumeration.BusApplicationStatus;
import b1nd.dodam.domain.rds.bus.enumeration.BusStatus;
import b1nd.dodam.domain.rds.bus.repository.BusApplicationRepository;
import b1nd.dodam.domain.rds.bus.repository.BusPresetRepository;
import b1nd.dodam.domain.rds.bus.repository.BusRepository;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.firebase.client.FCMClient;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.bus.application.data.req.BusPresetReq;
import b1nd.dodam.restapi.bus.application.data.req.BusReq;
import b1nd.dodam.restapi.bus.application.data.req.BusWithPresetReq;
import b1nd.dodam.restapi.bus.application.data.res.BusMemberRes;
import b1nd.dodam.restapi.bus.application.data.res.BusRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusUseCase {

    private final BusRepository busRepository;
    private final BusApplicationRepository busApplicationRepository;
    private final StudentRepository studentRepository;
    private final BusPresetRepository busPresetRepository;
    private final MemberAuthenticationHolder memberAuthenticationHolder;
    private final FCMClient fcmClient;

    @Transactional(rollbackFor = Exception.class)
    public Response register(BusReq req) {
        busRepository.save(req.mapToBus());
        List<String> pushTokens = studentRepository.findAllMembers().stream()
                .map(Member::getPushToken).toList();
        fcmClient.sendMessages(pushTokens, "귀가버스 신청", "귀가 버스 신청이 가능해요! 신청해주세요.");
        return Response.created("버스 등록 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response register(int presetId, BusWithPresetReq req) {
        BusPreset busPreset = busPresetRepository.getById(presetId);
        busRepository.save(req.mapToBus(busPreset, req.leaveTime()));
        List<String> pushTokens = studentRepository.findAllMembers().stream()
                .map(Member::getPushToken).toList();
        fcmClient.sendMessages(pushTokens, "귀가버스 신청", "귀가 버스 신청이 가능해요! 신청해주세요.");
        return Response.created("버스 등록 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response registerPreset(BusPresetReq req){
        busPresetRepository.save(req.mapToBusPreset());
        return Response.created("버스 프리셋 생성 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response modifyStatus(int id, BusStatus status){
        Bus bus = busRepository.getById(id);
        if (bus.equals(BusStatus.ACTIVATE)) {
            BusApplication busApplication = busApplicationRepository.getBusApplicationByBus(bus);
            busApplication.updateStatus(BusApplicationStatus.EXPIRED);
        }
        bus.setStatus(status);
        return Response.noContent("버스 상태 변경 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response modify(int id, BusReq req) {
        Bus bus = busRepository.getByIdForUpdate(id);
        bus.updateBus(req.busName(), req.description(), req.peopleLimit(), req.leaveTime(), req.timeRequired());
        return Response.noContent("버스 수정 성공");
    }

    public ResponseData<List<Bus>> getValid() {
        return ResponseData.ok("유효 버스 조회 성공", busRepository.findBusByStatus(BusStatus.ACTIVATE));
    }

    public ResponseData<List<BusRes>> getAll(int page, int limit) {
        return ResponseData.ok("버스 조회 성공", parseToBusList(busRepository.findAllByOrderByIdDesc(PageRequest.of(page - 1, limit))));
    }

    public ResponseData<List<BusRes>> getByDate(int year, int month, int day) {
        return ResponseData.ok("해당 날짜의 버스 조회 성공", parseToBusList(busRepository.findAllByLeaveTime(LocalDate.of(year, month, day))));
    }

    private List<BusRes> parseToBusList(List<Bus> buses) {
        return buses.stream()
                .map(bus -> BusRes.createFromBus(
                        bus,
                        getApplications(bus).stream()
                                .map(BusMemberRes::createFromBusMember)
                                .toList()
                        )
                ).toList();
    }

    private List<BusApplication> getApplications(Bus bus) {
        return busApplicationRepository.findByBusOrderByStudentAsc(bus);
    }

    public ResponseData<Bus> getMy() {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        return ResponseData.ok("신청한 버스 조회 성공",
                busApplicationRepository.findBusByStatusAndStudent(
                        BusStatus.ACTIVATE,
                        ZonedDateTimeUtil.nowToLocalDateTime(),
                        student.getId()
                )
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public Response delete(int id) {
        busRepository.deleteById(id);
        return Response.noContent("버스 삭제 성공");
    }

}
