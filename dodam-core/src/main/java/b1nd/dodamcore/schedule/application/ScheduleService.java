package b1nd.dodamcore.schedule.application;

import b1nd.dodamcore.common.enums.TargetGrade;
import b1nd.dodamcore.common.util.ModifyUtil;
import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.schedule.application.dto.req.ScheduleReq;
import b1nd.dodamcore.schedule.domain.entity.Schedule;
import b1nd.dodamcore.schedule.domain.exception.ScheduleNotFoundException;
import b1nd.dodamcore.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
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
        try {
            scheduleRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ScheduleNotFoundException();
        }
    }

    public List<Schedule> getSchedules(int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit);
        return scheduleRepository.findAllByOrderByIdDesc(pageRequest);
    }

    public List<Schedule> getScheduleByDate(LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findByDateBetween(startDate, endDate);
    }

    public List<Schedule> getScheduleByKeyword(String keyword) {
        return scheduleRepository.findByNameContaining(keyword);
    }

    public List<Schedule> getTodaySchedule() {
        return scheduleRepository.findByDate(ZonedDateTimeUtil.nowToLocalDate());
    }
}
