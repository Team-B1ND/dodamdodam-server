package b1nd.dodam.restapi.bus.application;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.entity.BusApplication;
import b1nd.dodam.domain.rds.bus.entity.BusTime;
import b1nd.dodam.domain.rds.bus.entity.BusTimeToBus;
import b1nd.dodam.domain.rds.bus.enumeration.BusApplicationStatus;
import b1nd.dodam.domain.rds.bus.enumeration.BusStatus;
import b1nd.dodam.domain.rds.bus.exception.BusPresetNameDuplicated;
import b1nd.dodam.domain.rds.bus.repository.*;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.process.listener.pushalarm.FcmEvent;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.bus.application.data.req.BusPresetReq;
import b1nd.dodam.restapi.bus.application.data.req.BusReq;
import b1nd.dodam.restapi.bus.application.data.req.BusTimeReq;
import b1nd.dodam.restapi.bus.application.data.res.BusMemberRes;
import b1nd.dodam.restapi.bus.application.data.res.BusPresetRes;
import b1nd.dodam.restapi.bus.application.data.res.BusRes;
import b1nd.dodam.restapi.bus.application.data.res.BusTimeRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final BusTimeRepository busTimeRepository;
    private final BusTimeToBusRepository busTimeToBusRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(rollbackFor = Exception.class)
    public Response register(BusReq req) {
        busRepository.save(req.mapToBus());
        return Response.created("버스 등록 성공");
    }

    private void registerBus(){
        List<String> pushTokens = studentRepository.findAllMembers().stream()
                .map(Member::getPushToken).toList();
        pushTokens.parallelStream().forEach(token ->
            eventPublisher.publishEvent(new FcmEvent(token, "귀가버스 신청", "귀가 버스 신청이 가능해요! 신청해주세요."))
        );

    }

    @Transactional(rollbackFor = Exception.class)
    public Response registerPreset(BusPresetReq req){
        if (busPresetRepository.existsByName(req.busName()))
            throw new BusPresetNameDuplicated();
        else busPresetRepository.save(req.mapToBusPreset());
        return Response.created("버스 프리셋 생성 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response registerTime(BusTimeReq req) {
        BusTime busTime = busTimeRepository.save(req.mapToBusTime());
        List<Bus> buses = busRepository.findAllById(req.busId());
        busTimeToBusRepository.saveAll(req.mapBusTimeToBus(busTime, buses));
        registerBus();
        return Response.created("버스 신청 기간 생성 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response modifyStatus(int id, BusStatus status){
        Bus bus = busRepository.getById(id);
        checkStatusForUpdate(bus);
        bus.setStatus(status);
        return Response.noContent("버스 상태 변경 성공");
    }

    private void checkStatusForUpdate(Bus bus){
        if (bus.equals(BusStatus.ACTIVATE)) {
            BusApplication busApplication = busApplicationRepository.getBusApplicationByBus(bus);
            busApplication.updateStatus(BusApplicationStatus.EXPIRED);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response modify(int id, BusReq req) {
        Bus bus = busRepository.getByIdForUpdate(id);
        bus.updateBus(req.busName(), req.description(), req.peopleLimit(), req.leaveAt(), req.leaveTime(), req.timeRequired());
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
                        ZonedDateTimeUtil.nowToLocalDate(),
                        ZonedDateTimeUtil.nowToLocalTime(),
                        student.getId()
                )
        );
    }

    public ResponseData<List<BusPresetRes>> getAllBusPreset(){
        List<BusPresetRes> busPresetResList = busPresetRepository.findAll()
                .stream()
                .map(BusPresetRes::of)
                .toList();
        return ResponseData.ok("프리셋 전체 조회 성공", busPresetResList);
    }

    public ResponseData<BusPresetRes> getBusPresetInfo(int id){
        BusPresetRes busPresetRes = BusPresetRes.of(busPresetRepository.getById(id));
        return ResponseData.ok("프리셋 정보 조회 성공", busPresetRes);
    }

    public ResponseData<List<BusTimeRes>> getAllBusTime(){
        List<BusTimeRes> busTimeRes = busTimeRepository.findAll().stream()
                .map(BusTimeRes::of)
                .toList();
        return ResponseData.ok("버스 기간 불러오기 성공", busTimeRes);
    }

    public ResponseData<List<BusRes>> getBusByBusTime(int id) {
        BusTime busTime = busTimeRepository.getById(id);
        List<Bus> buses = getBusesByBusTime(busTime);
        List<BusRes> busRes = convertToBusRes(buses);
        return ResponseData.ok("버스 불러오기 성공", busRes);
    }

    private List<Bus> getBusesByBusTime(BusTime busTime) {
        return busTimeToBusRepository.findAllByBusTime(busTime)
                .stream()
                .map(BusTimeToBus::getBus)
                .toList();
    }

    private List<BusRes> convertToBusRes(List<Bus> buses) {
        return buses.stream()
                .map(bus -> BusRes.createFromBus(bus, getApplications(bus).stream()
                        .map(BusMemberRes::createFromBusMember)
                                .toList()))
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Response delete(int id) {
        busTimeToBusRepository.deleteBusTimeToBusByBus_Id(id);
        busApplicationRepository.deleteAllByBus_Id(id);
        busRepository.deleteById(id);
        return Response.noContent("버스 삭제 성공");
    }

}
