package b1nd.dodamapi.bus.usecase;

import b1nd.dodamapi.bus.usecase.dto.req.BusReq;
import b1nd.dodamapi.bus.usecase.dto.res.BusMemberRes;
import b1nd.dodamapi.bus.usecase.dto.res.BusRes;
import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.bus.application.BusApplicationService;
import b1nd.dodamcore.bus.application.BusService;
import b1nd.dodamcore.bus.domain.entity.Bus;
import b1nd.dodamcore.bus.domain.entity.BusMember;
import b1nd.dodamcore.common.util.ModifyUtil;
import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusUseCase {

    private final BusService busService;
    private final BusApplicationService busApplicationService;
    private final MemberService memberService;

    @Transactional(rollbackFor = Exception.class)
    public Response register(BusReq req) {
        busService.save(req.mapToBus());
        return Response.created("버스 등록 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response modify(int id, BusReq modifyBusReq) {
        Bus bus = busService.getByIdForUpdate(id);
        bus.updateBus(
                ModifyUtil.modifyIfNotNull(modifyBusReq.busName(), bus.getBusName()),
                ModifyUtil.modifyIfNotNull(modifyBusReq.description(), bus.getDescription()),
                ModifyUtil.modifyIfNotNull(modifyBusReq.leaveTime(), bus.getLeaveTime()),
                ModifyUtil.modifyIfNotNull(modifyBusReq.timeRequired(), bus.getTimeRequired()),
                ModifyUtil.modifyIfNotZero(modifyBusReq.peopleLimit(), bus.getPeopleLimit())
        );
        return Response.noContent("버스 수정 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response delete(int id) {
        Bus bus = busService.getById(id);
        busService.delete(bus);
        return Response.noContent("버스 삭제 성공");
    }

    public ResponseData<List<Bus>> getValid() {
        LocalDateTime now = ZonedDateTimeUtil.nowToLocalDateTime();
        return ResponseData.ok("유효 버스 조회 성공", busService.getValid(now, now.plusDays(7)));
    }

    public ResponseData<List<BusRes>> getBuses(int page, int limit) {
        return ResponseData.ok("버스 조회 성공", parseToBusList(busService.getAll(page, limit)));
    }

    public ResponseData<List<BusRes>> getBusesByDate(int year, int month, int day) {
        return ResponseData.ok("해당 날짜의 버스 조회 성공", parseToBusList(busService.getAllByDate(LocalDate.of(year, month, day))));
    }

    private List<BusRes> parseToBusList(List<Bus> buses) {
        return buses.stream()
                .map(bus -> BusRes.createFromBus(
                        bus,
                        getBusApplications(bus).stream()
                                .map(BusMemberRes::createFromBusMember)
                                .toList()
                        )
                ).toList();
    }

    private List<BusMember> getBusApplications(Bus bus) {
        return busApplicationService.getByBus(bus);
    }

    public ResponseData<Bus> getMy() {
        int studentId = memberService.getStudentFromSession().getId();
        return ResponseData.ok("신청한 버스 조회 성공", busService.getByStudent(ZonedDateTimeUtil.nowToLocalDateTime(), studentId));
    }

}
