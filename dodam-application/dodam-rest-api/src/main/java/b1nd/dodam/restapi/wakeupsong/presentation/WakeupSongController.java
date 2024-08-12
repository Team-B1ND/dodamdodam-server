package b1nd.dodam.restapi.wakeupsong.presentation;

import b1nd.dodam.melon.chart.client.data.res.ChartRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import b1nd.dodam.restapi.wakeupsong.application.WakeupSongUseCase;
import b1nd.dodam.restapi.wakeupsong.application.data.req.ApplyWakeupSongBySearchReq;
import b1nd.dodam.restapi.wakeupsong.application.data.req.ApplyWakeupSongReq;
import b1nd.dodam.restapi.wakeupsong.application.data.res.WakeupSongRes;
import b1nd.dodam.youtube.video.client.data.YoutubeRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/wakeup-song")
@RequiredArgsConstructor
@Slf4j
public class WakeupSongController {
    private final WakeupSongUseCase wakeupSongUseCase;

    @GetMapping("/allowed")
    public ResponseData<List<WakeupSongRes>> getAllowedWakeupSongByPlayDate(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day
    ) {
        return wakeupSongUseCase.getAllowedWakeupSong(year, month, day);
    }

    @GetMapping("/pending")
    public ResponseData<List<WakeupSongRes>> getPendingWakeupSong() {
        return wakeupSongUseCase.getPendingWakeupSong();
    }

    @GetMapping("/my")
    public ResponseData<List<WakeupSongRes>> getMyWakeupSong() {
        return wakeupSongUseCase.getMyWakeupSong();
    }

    @PostMapping
    public CompletableFuture<Response> createWakeupSong(@RequestBody @Valid ApplyWakeupSongReq req) {
        return wakeupSongUseCase.createWakeupSong(req.videoUrl());
    }

    @PostMapping("/keyword")
    public CompletableFuture<Response> createWakeupSongByYoutubeSearch(@RequestBody @Valid ApplyWakeupSongBySearchReq req) {
        return wakeupSongUseCase.createWakeupSongByYoutubeSearch(req);
    }

    @GetMapping("/search")
    public ResponseData<List<YoutubeRes>> getYoutubeVideo(@RequestParam String keyword) {
        return wakeupSongUseCase.getYoutubeList(keyword);
    }

    @PatchMapping("/allow/{id}")
    public Response allowWakeupSong(@PathVariable int id) {
        return wakeupSongUseCase.allow(id);
    }

    @PatchMapping("/deny/{id}")
    public Response denyWakeupSong(@PathVariable int id) {
        return wakeupSongUseCase.deny(id);
    }

    @DeleteMapping("/{id}")
    public Response deleteWakeupSong(@PathVariable int id) {
        return wakeupSongUseCase.delete(id);
    }

    @DeleteMapping("/my/{id}")
    public Response deleteMyWakeupSong(@PathVariable int id) {
        return wakeupSongUseCase.deleteMyWakeupSong(id);
    }

    @GetMapping("/chart")
    public CompletableFuture<ResponseData<List<ChartRes>>> getChart() {
        return wakeupSongUseCase.getChartList();
    }

}
