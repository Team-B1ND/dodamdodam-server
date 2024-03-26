package b1nd.dodamcore.schedule.application;

import b1nd.dodamcore.common.enums.TargetGrade;
import b1nd.dodamcore.common.util.ModifyUtil;
import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.schedule.application.dto.req.ScheduleReq;
import b1nd.dodamcore.schedule.application.dto.res.ScheduleRes;
import b1nd.dodamcore.schedule.domain.entity.Schedule;
import b1nd.dodamcore.schedule.domain.exception.ScheduleNotFoundException;
import b1nd.dodamcore.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional(rollbackFor = Exception.class)
    public void createSchedule(ScheduleReq createScheduleReq) {
        Schedule schedule = createScheduleReq.mapToSchedule();
        scheduleRepository.save(schedule);
    }

    @Transactional(rollbackFor = Exception.class)
    public void modifySchedule(int id, ScheduleReq modifyScheduleReq) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(ScheduleNotFoundException::new);

        Set<String> modifiedGrades = modifyScheduleReq.grades();
        Set<TargetGrade> targetGrades = modifiedGrades != null ? modifiedGrades.stream().map(TargetGrade::of).collect(Collectors.toSet()) : Collections.emptySet();

        schedule.updateSchedule(
                ModifyUtil.modifyIfNotNull(modifyScheduleReq.name(), schedule.getName()),
                ModifyUtil.modifyIfNotNull(modifyScheduleReq.place(), schedule.getPlace().getPlace()),
                ModifyUtil.modifyIfNotNull(modifyScheduleReq.startDate(), schedule.getStartDate()),
                ModifyUtil.modifyIfNotNull(modifyScheduleReq.endDate(), schedule.getEndDate()),
                ModifyUtil.modifyIfNotNull(targetGrades, schedule.getTargetGrades())
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteSchedule(int id) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(ScheduleNotFoundException::new);

        scheduleRepository.delete(schedule);
    }

    public List<ScheduleRes> getSchedules(int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit);
        return scheduleRepository.findAllByOrderByIdDesc(pageRequest)
                .stream().map(ScheduleRes::of).collect(Collectors.toList());
    }

    public List<ScheduleRes> getScheduleByPeriod(LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findByDateBetween(startDate, endDate)
                .stream().map(ScheduleRes::of).collect(Collectors.toList());
    }

    public List<ScheduleRes> getScheduleByKeyword(String keyword) {
        return scheduleRepository.findByNameContaining(keyword)
                .stream().map(ScheduleRes::of).collect(Collectors.toList());
    }

    public List<ScheduleRes> getTodaySchedule() {
        return scheduleRepository.findByDate(ZonedDateTimeUtil.nowToLocalDate())
                .stream().map(ScheduleRes::of).collect(Collectors.toList());
    }

    public List<ScheduleRes> getScheduleByDate(int year, int month, int day) {
        return scheduleRepository.findByDate(LocalDate.of(year, month, day))
                .stream().map(ScheduleRes::of).collect(Collectors.toList());
    }
}
