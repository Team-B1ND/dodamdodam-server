package b1nd.dodamcore.bus.application;

import b1nd.dodamcore.bus.application.dto.req.BusReq;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusService {

    private final MemberSessionHolder memberSessionHolder;
    private final BusRepository busRepository;
    private final BusMemberRepository busMemberRepository;
    private final StudentRepository studentRepository;

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

        //TODO: private 메서드화, 중복되는 로직
        Bus bus = busRepository.findById(id)
                .orElseThrow(BusNotFoundException::new);
        //TODO: studentService Exception 처리
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
