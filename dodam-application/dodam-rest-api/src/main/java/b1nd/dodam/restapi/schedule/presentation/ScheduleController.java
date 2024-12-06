package b1nd.dodam.restapi.schedule.presentation;

import b1nd.dodam.restapi.schedule.application.ScheduleUseCase;
import b1nd.dodam.restapi.schedule.application.data.req.ScheduleReq;
import b1nd.dodam.restapi.schedule.application.data.res.ScheduleRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleUseCase useCase;

    @PostMapping
    public Response create(@RequestBody @Valid ScheduleReq req) {
        return useCase.create(req);
    }

    @PatchMapping("/{id}")
    public Response modify(@PathVariable int id, ScheduleReq req) {
        return useCase.modify(id, req);
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable int id) {
        return useCase.delete(id);
    }

    @GetMapping("/today")
    public ResponseData<List<ScheduleRes>> getToday() {
        return useCase.getToday();
    }

    @GetMapping("/date")
    public ResponseData<List<ScheduleRes>> getByDate(@RequestParam int year,
                                                     @RequestParam int month,
                                                     @RequestParam int day) {
        return useCase.getByDate(year, month, day);
    }

    @GetMapping("/search")
    public ResponseData<List<ScheduleRes>> getByDate(@RequestParam LocalDate startAt,
                                                     @RequestParam LocalDate endAt) {
        return useCase.getByDate(startAt, endAt);
    }

    @GetMapping("/search/keyword")
    public ResponseData<List<ScheduleRes>> searchByName(@RequestParam("keyword") String keyword) {
        return useCase.searchByName(keyword);
    }

    @GetMapping
    public ResponseData<List<ScheduleRes>> getAllOrderByIdDesc(@RequestParam int page,
                                                  @RequestParam int size) {
        return useCase.getAllOrderByIdDesc(page, size);
    }

    @PostMapping("/neis")
    public Response createByNeis(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ){
        return useCase.createScheduleByNeis(startDate, endDate);
    }

}
