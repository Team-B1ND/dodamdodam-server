package b1nd.dodamapi.wakeupsong.handler;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamapi.wakeupsong.usecase.WakeupSongUseCase;
import b1nd.dodamcore.wakeupsong.application.WakeupSongService;
import b1nd.dodamcore.wakeupsong.application.dto.req.ApplyWakeupSongBySearchReq;
import b1nd.dodamcore.wakeupsong.application.dto.req.ApplyWakeupSongReq;
import b1nd.dodamcore.wakeupsong.application.dto.res.ChartRes;
import b1nd.dodamcore.wakeupsong.application.dto.res.WakeupSongRes;
import b1nd.dodamcore.wakeupsong.application.dto.res.YoutubeRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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
    public ResponseData<CompletableFuture<List<ChartRes>>> getChart() {
        return wakeupSongUseCase.getChartList();
    }
}
