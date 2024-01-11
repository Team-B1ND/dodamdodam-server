package b1nd.dodamcore.bus.application;

import b1nd.dodamcore.bus.application.dto.req.BusReq;
import b1nd.dodamcore.bus.application.dto.res.BusMemberRes;
import b1nd.dodamcore.bus.application.dto.res.BusRes;
import b1nd.dodamcore.bus.domain.entity.Bus;
import b1nd.dodamcore.bus.domain.entity.BusMember;
import b1nd.dodamcore.bus.domain.exception.*;
import b1nd.dodamcore.bus.repository.BusMemberRepository;
import b1nd.dodamcore.bus.repository.BusRepository;
import b1nd.dodamcore.common.util.ModifyUtil;
import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.member.application.MemberSessionHolder;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusService {

    private final MemberSessionHolder memberSessionHolder;
    private final BusRepository busRepository;
    private final BusMemberRepository busMemberRepository;
    private final StudentRepository studentRepository;

    public List<Bus> getValidBuses() {

        LocalDateTime now = ZonedDateTimeUtil.nowToLocalDateTime();
        List<Bus> buses = busRepository.findBusByLeaveTimeBetween(now, now.plusDays(7));

        if(buses.size() == 0) return new ArrayList<>();
        else return buses;
    }

    public List<BusRes> getBuses(int page, int limit) {

        PageRequest pageRequest = PageRequest.of(page - 1, limit);
        List<Bus> buses = busRepository.findAllByOrderByIdDesc(pageRequest);

        return parseToBusList(buses);
    }

    public List<BusRes> getBusesByDate(int year, int month, int day) {

        String yearStr = String.format("%04d", year);
        String monthStr = String.format("%02d", month);
        String dayStr = String.format("%02d", day);

        List<Bus> buses = busRepository.findAllByLeaveTime(LocalDate.parse(yearStr + "-" + monthStr + "-" + dayStr));

        return parseToBusList(buses);
    }

    private List<BusRes> parseToBusList(List<Bus> buses) {
        return buses.stream().map(bus ->
                BusRes.createFromBus(
                        bus,
                        getBusMembers(bus).stream()
                                .map(BusMemberRes::createFromBusMember)
                                .collect(Collectors.toList())
                )
        ).collect(Collectors.toList());
    }

    private List<BusMember> getBusMembers(Bus bus) {
        return busMemberRepository.findByBusOrderByStudentAsc(bus);
    }

    public Bus getAppliedBus() {
        Student student = studentRepository.findByMember(memberSessionHolder.current())
                .orElseThrow(RuntimeException::new);
        return busRepository.findBusByStudent(LocalDateTime.now(), student.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void createBus(BusReq createBusReq) {
        busRepository.save(createBusReq.mapToBus());
    }

    @Transactional(rollbackFor = Exception.class)
    public void modifyBus(int id, BusReq modifyBusReq) {

        Bus bus = busRepository.findById(id)
                .orElseThrow(BusNotFoundException::new);

        if(bus.getLeaveTime().isAfter(ZonedDateTimeUtil.nowToLocalDateTime())) {
            throw new BusPeriodExpiredException();
        }

        bus.updateBus(
                ModifyUtil.modifyIfNotNull(modifyBusReq.busName(), bus.getBusName()),
                ModifyUtil.modifyIfNotNull(modifyBusReq.description(), bus.getDescription()),
                ModifyUtil.modifyIfNotNull(modifyBusReq.leaveTime(), bus.getLeaveTime()),
                ModifyUtil.modifyIfNotNull(modifyBusReq.timeRequired(), bus.getTimeRequired()),
                ModifyUtil.modifyIfNotZero(modifyBusReq.peopleLimit(), bus.getPeopleLimit())
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBus(int id) {
        try {
            busRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new BusNotFoundException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void applyBus(int id) {

        Bus bus = busRepository.findById(id)
                .orElseThrow(BusNotFoundException::new);
        //TODO: studentService Exception 처리 - 중복되는 로직
        Student student = studentRepository.findByMember(memberSessionHolder.current())
                .orElseThrow(RuntimeException::new);

        bus.increaseApplyCount();

        if(bus.getPeopleLimit() <= bus.getApplyCount()) {
            throw new BusFullOfSeatException();
        }
        if(bus.getLeaveTime().isBefore(ZonedDateTimeUtil.nowToLocalDateTime())) {
            throw new BusPeriodExpiredException();
        }
        if(busMemberRepository.existsByStudentAndBus_LeaveTimeAfter(student, ZonedDateTimeUtil.nowToLocalDateTime())) {
            throw new BusAlreadyApplyException();
        }

        BusMember busMember = BusMember.builder()
                .student(student)
                .bus(bus)
                .build();
        busMemberRepository.save(busMember);
    }

    @Transactional(rollbackFor = Exception.class)
    public void modifyAppliedBus(int id) {

        Bus changeBus = busRepository.findById(id)
                .orElseThrow(BusNotFoundException::new);
        Student student = studentRepository.findByMember(memberSessionHolder.current())
                .orElseThrow(RuntimeException::new);

        if(changeBus.getLeaveTime().isBefore(ZonedDateTimeUtil.nowToLocalDateTime())) {
            throw new BusPeriodExpiredException();
        }

        busMemberRepository.findByStudentAndBus_LeaveTimeAfter(student, ZonedDateTimeUtil.nowToLocalDateTime()).ifPresentOrElse(
                busMember -> {
                    busMember.getBus().decreaseApplyCount();
                    busMember.modifyBus(changeBus);
                    changeBus.increaseApplyCount();
                },
                () -> {throw new BusMemberNotFoundException();}
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelAppliedBus(int id) {

        Student student = studentRepository.findByMember(memberSessionHolder.current())
                .orElseThrow(RuntimeException::new);

        busMemberRepository.findByStudentAndBus_Id(student, id).ifPresentOrElse(
                busMember -> {
                    busMember.getBus().decreaseApplyCount();
                    busMemberRepository.delete(busMember);
                },
                () -> {throw new BusMemberNotFoundException();}
        );
    }
}
