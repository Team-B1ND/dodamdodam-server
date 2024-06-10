package b1nd.dodamapi.schedule;

import b1nd.dodamcore.common.response.Response;
import b1nd.dodamcore.common.response.ResponseData;
import b1nd.dodamcore.schedule.application.ScheduleService;
import b1nd.dodamcore.schedule.application.dto.req.ScheduleReq;
import b1nd.dodamcore.schedule.application.dto.res.ScheduleRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public Response createSchedule(@RequestBody @Valid ScheduleReq createScheduleReq) {
        scheduleService.createSchedule(createScheduleReq);
        return Response.created("일정 생성 성공");
    }

    @PatchMapping("/{id}")
    public Response modifySchedule(
            @PathVariable int id,
            @RequestBody ScheduleReq modifyScheduleReq
    ) {
        scheduleService.modifySchedule(id, modifyScheduleReq);
        return Response.ok("일정 수정 성공");
    }

    @DeleteMapping("/{id}")
    public Response deleteSchedule(@PathVariable int id) {
        scheduleService.deleteSchedule(id);
        return Response.ok("일정 삭제 성공");
    }

    @GetMapping//테이블뷰로보기 - Teacher
    public ResponseData<List<ScheduleRes>> getSchedules(
            @RequestParam int page,
            @RequestParam int limit
    ) {
        List<ScheduleRes> scheduleList = scheduleService.getSchedules(page, limit);
        return ResponseData.ok("일정 조회 성공", scheduleList);
    }

    @GetMapping("/search")
    public ResponseData<List<ScheduleRes>> getScheduleByDate(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        List<ScheduleRes> scheduleList = scheduleService.getScheduleByPeriod(startDate, endDate);
        return ResponseData.ok("기간 내의 일정 조회 성공", scheduleList);
    }

    @GetMapping("/search/keyword")
    public ResponseData<List<ScheduleRes>> getScheduleByKeyword(@RequestParam("keyword") String keyword) {
        List<ScheduleRes> scheduleList = scheduleService.getScheduleByKeyword(keyword);
        return ResponseData.ok("해당 키워드의 일정 조회 성공", scheduleList);
    }

    @GetMapping("/today")
    public ResponseData<List<ScheduleRes>> getTodaySchedule() {
        List<ScheduleRes> scheduleList = scheduleService.getTodaySchedule();
        return ResponseData.ok("오늘 일정 조회 성공", scheduleList);
    }

    @GetMapping("/date")
    public ResponseData<List<ScheduleRes>> getScheduleByDate(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day
    ) {
        List<ScheduleRes> scheduleList = scheduleService.getScheduleByDate(year, month, day);
        return ResponseData.ok("해당 날짜의 일정 조회 성공", scheduleList);
    }
}
