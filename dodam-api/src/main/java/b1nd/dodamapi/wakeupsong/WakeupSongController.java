package b1nd.dodamapi.wakeupsong;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.wakeupsong.application.WakeupSongService;
import b1nd.dodamcore.wakeupsong.application.dto.req.ApplyWakeupSongBySearchReq;
import b1nd.dodamcore.wakeupsong.application.dto.req.ApplyWakeupSongReq;
import b1nd.dodamcore.wakeupsong.application.dto.res.ChartRes;
import b1nd.dodamcore.wakeupsong.application.dto.res.WakeupSongRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/wakeup-song")
@RequiredArgsConstructor
public class WakeupSongController {

    private final WakeupSongService wakeupSongService;

    @GetMapping("/allowed")
    public ResponseData<List<WakeupSongRes>> getAllowedWakeupSongByPlayDate(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day
    ) {
        List<WakeupSongRes> wakeupSongList = wakeupSongService.getAllowedWakeupSongByPlayDate(year, month, day);
        return ResponseData.ok("승인된 기상송 조회 성공", wakeupSongList);
    }

    @GetMapping("/pending")
    public ResponseData<List<WakeupSongRes>> getPendingWakeupSong() {
        List<WakeupSongRes> wakeupSongList = wakeupSongService.getPendingWakeupSong();
        return ResponseData.ok("승인 대기 중인 기상송 조회 성공", wakeupSongList);
    }

    @GetMapping("/my")
    public ResponseData<List<WakeupSongRes>> getMyWakeupSong() {
        List<WakeupSongRes> wakeupSongList = wakeupSongService.getMyWakeupSong();
        return ResponseData.ok("자신이 신청한 기상송 조회 성공", wakeupSongList);
    }

    @PostMapping
    public Callable<Response> createWakeupSong(@RequestBody @Valid ApplyWakeupSongReq req) {
        wakeupSongService.createWakeupSong(req.videoUrl());
        return () -> Response.created("기상송 신청 성공");
    }

    @PostMapping("/keyword")
    public Callable<Response> createWakeupSongByYoutubeSearch(@RequestBody @Valid ApplyWakeupSongBySearchReq req) {
        wakeupSongService.createWakeupSongByYoutubeSearch(req);
        return () -> Response.created("유튜브 검색을 통한 기상송 신청 성공");
    }

    @PatchMapping("/allow/{id}")
    public Response allowWakeupSong(@PathVariable int id) {
        wakeupSongService.allowWakeupSong(id);
        return Response.ok("기상송 승인 성공");
    }

    @PatchMapping("/deny/{id}")
    public Response denyWakeupSong(@PathVariable int id) {
        wakeupSongService.denyWakeupSong(id);
        return Response.ok("기상송 거절 성공");
    }

    @DeleteMapping("/{id}")
    public Response deleteWakeupSong(@PathVariable int id) {
        wakeupSongService.deleteWakeupSong(id);
        return Response.ok("기상송 삭제 성공");
    }

    @DeleteMapping("/my/{id}")
    public Response deleteMyWakeupSong(@PathVariable int id) {
        wakeupSongService.deleteMyWakeupSong(id);
        return Response.ok("기상송 신청 취소 성공");
    }

    @GetMapping("/chart")
    public ResponseData<List<ChartRes>> getChart() {
        List<ChartRes> chartRoList = wakeupSongService.getChartList();
        return ResponseData.ok("차트 조회 성공", chartRoList);
    }
}
