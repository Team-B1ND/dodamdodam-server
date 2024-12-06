package b1nd.dodam.restapi.schedule.application;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.schedule.entity.Schedule;
import b1nd.dodam.domain.rds.schedule.enumeration.TargetGrade;
import b1nd.dodam.domain.rds.schedule.service.ScheduleService;
import b1nd.dodam.neis.schedule.client.NeisScheduleClient;
import b1nd.dodam.neis.schedule.client.data.NeisSchedule;
import b1nd.dodam.restapi.schedule.application.data.req.ScheduleReq;
import b1nd.dodam.restapi.schedule.application.data.res.ScheduleRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleUseCase {

    private final ScheduleService scheduleService;
    private final NeisScheduleClient neisScheduleClient;

    @Transactional(rollbackFor = Exception.class)
    public Response create(ScheduleReq req) {
        scheduleService.save(req.mapToSchedule());
        return Response.created("일정 생성 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response modify(int id, ScheduleReq req) {
        Schedule schedule = scheduleService.getById(id);
        Set<String> modifiedGrades = req.grades();
        Set<TargetGrade> targetGrades = modifiedGrades != null ? modifiedGrades.stream().map(TargetGrade::of).collect(Collectors.toSet()) : Collections.emptySet();
        schedule.updateSchedule(req.name(), req.place(), req.startDate(), req.endDate(), targetGrades);
        return Response.noContent("일정 수정 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response delete(int id) {
        scheduleService.deleteById(id);
        return Response.noContent("일정 삭제 성공");
    }

    public ResponseData<List<ScheduleRes>> getAllOrderByIdDesc(int page, int size) {
        return ResponseData.ok("일정 조회 성공", ScheduleRes.of(scheduleService.getAllOrderByIdDesc(page, size)));
    }

    public ResponseData<List<ScheduleRes>> getByDate(LocalDate startAt, LocalDate endAt) {
        return ResponseData.ok("기간 내의 일정 조회 성공", ScheduleRes.of(scheduleService.getByPeriod(startAt, endAt)));
    }

    public ResponseData<List<ScheduleRes>> searchByName(String name) {
        return ResponseData.ok("해당 키워드의 일정 조회 성공", ScheduleRes.of(scheduleService.searchByName(name)));
    }

    public ResponseData<List<ScheduleRes>> getToday() {
        return ResponseData.ok("오늘의 일정 조회 성공", ScheduleRes.of(scheduleService.getByDate(ZonedDateTimeUtil.nowToLocalDate())));
    }

    public ResponseData<List<ScheduleRes>> getByDate(int year, int month, int day) {
        return ResponseData.ok("해당 날짜의 일정 조회 성공", ScheduleRes.of(scheduleService.getByDate(LocalDate.of(year, month, day))));
    }

    @Transactional(rollbackFor = Exception.class)
    public Response createScheduleByNeis(LocalDate startDate, LocalDate endDate){
        List<NeisSchedule> neisSchedule = neisScheduleClient.getSchedules(startDate, endDate);
        List<Schedule> schedules = neisSchedule
                .parallelStream()
                .map(this::scheduleBuilderForNeis)
                .toList();
        scheduleService.saveAll(schedules);
        return ResponseData.ok("neis로 일정 저장 성공");
    }

    private Schedule scheduleBuilderForNeis(NeisSchedule neisSchedule){
        return Schedule.builder()
                .name(neisSchedule.eventName())
                .startDate(neisSchedule.startDate())
                .endDate(neisSchedule.endDate())
                .targetGrades(neisSchedule.grades().stream().map(TargetGrade::of).collect(Collectors.toSet()))
                .build();
    }

}
