package b1nd.dodamapi.wakeupsong.usecase;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.common.util.YoutubeApiUtil;
import b1nd.dodamcore.member.application.MemberSessionHolder;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.wakeupsong.application.ChartClient;
import b1nd.dodamcore.wakeupsong.application.WakeupSongClient;
import b1nd.dodamcore.wakeupsong.application.WakeupSongService;
import b1nd.dodamcore.wakeupsong.application.dto.req.ApplyWakeupSongBySearchReq;
import b1nd.dodamcore.wakeupsong.application.dto.res.ChartRes;
import b1nd.dodamcore.wakeupsong.application.dto.res.WakeupSongRes;
import b1nd.dodamcore.wakeupsong.application.dto.res.YoutubeApiRes;
import b1nd.dodamcore.wakeupsong.application.dto.res.YoutubeRes;
import b1nd.dodamcore.wakeupsong.domain.entity.WakeupSong;
import b1nd.dodamcore.wakeupsong.domain.exception.UnsupportedVideoTypeException;
import b1nd.dodamcore.wakeupsong.domain.exception.WakeupSongAlreadyCreatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class WakeupSongUseCase {
    private final WakeupSongService wakeupSongService;
    private final WakeupSongClient wakeupSongClient;
    private final ChartClient chartClient;
    private final MemberSessionHolder memberSessionHolder;

    public ResponseData<List<WakeupSongRes>> getAllowedWakeupSong(int year, int month, int day){
        List<WakeupSong> wakeupSongList = wakeupSongService.getByPlayDate(year, month, day);
        return ResponseData.ok("승인된 기상송 조회 성공", WakeupSongRes.of(wakeupSongList));
    }

    public ResponseData<List<WakeupSongRes>> getPendingWakeupSong(){
        List<WakeupSong> wakeupSongList = wakeupSongService.getPendingWakeupSong();
        return ResponseData.ok("승인 대기 중인 기상송 조회 성공", WakeupSongRes.of(wakeupSongList));
    }

    public ResponseData<List<WakeupSongRes>> getMyWakeupSong(){
        Member member = memberSessionHolder.current();
        List<WakeupSong> wakeupSongList = wakeupSongService.getMyWakeupSong(member);
        return ResponseData.ok("자신이 신청한 기상송 조회 성공", WakeupSongRes.of(wakeupSongList));
    }

    @Async
    public CompletableFuture<Response> createWakeupSong(String videoUrl) {
        return CompletableFuture.supplyAsync(() -> {
            Member member = verifyAlreadyAppliedFromSession();
            String videoId = YoutubeApiUtil.getVideoId(videoUrl);
            YoutubeApiRes.Snippet snippet = wakeupSongClient.getVideo(videoId).getItems().get(0).getSnippet();
            checkIsMV(snippet.getTitle());
            wakeupSongService.buildAndSaveWakeupSong(snippet, videoId, videoUrl, member);
            return Response.created("기상송 신청 성공");
        });
    }

    @Async
    public CompletableFuture<Response> createWakeupSongByYoutubeSearch(ApplyWakeupSongBySearchReq req){
        return CompletableFuture.supplyAsync(() -> {
            Member member = verifyAlreadyAppliedFromSession();
            YoutubeApiRes.SearchItem searchItem = wakeupSongClient.searchVideoByKeyword(
                    req.title() + " " + req.artist(), 1).getItems().get(0);
            checkIsMV(searchItem.getSnippet().getTitle());
            wakeupSongService.buildAndSaveWakeupSong(searchItem.getSnippet(), searchItem.getId().getVideoId(),
                    "https://www.youtube.com/watch?v=" + searchItem.getId().getVideoId(), member);
            return Response.created("유튜브 검색을 통한 기상송 신청 성공");
        });
    }

    private void checkIsMV(String title){
        if(title.contains("MV")){
            throw new UnsupportedVideoTypeException();
        }
    }

    private Member verifyAlreadyAppliedFromSession(){
        Member member = memberSessionHolder.current();
        if (wakeupSongService.existsByMemberAndCreatedAt(member)){
            throw new WakeupSongAlreadyCreatedException();
        } return member;
    }

    public Response allow(int id){
        WakeupSong wakeupSong = wakeupSongService.getById(id);
        wakeupSong.allow(memberSessionHolder.current());
        return Response.ok("기상송 승인 성공");
    }

    public Response deny(int id){
        WakeupSong wakeupSong = wakeupSongService.getById(id);
        wakeupSong.deny();
        return Response.ok("기상송 거절 성공");
    }

    public Response delete(int id){
        WakeupSong wakeupSong = wakeupSongService.getById(id);
        wakeupSongService.delete(wakeupSong);
        return Response.ok("기상송 삭제 성공");
    }

    public Response deleteMyWakeupSong(int id) {
        WakeupSong wakeupSong = wakeupSongService.getById(id);
        wakeupSong.isApplicant(memberSessionHolder.current());
        wakeupSongService.delete(wakeupSong);
        return Response.ok("기상송 신청 취소 성공");
    }

    public ResponseData<List<ChartRes>> getChartList() {
        List<ChartRes> chartList = chartClient.getList().join();
        return ResponseData.ok("차트 조회 성공", chartList);
    }

    public ResponseData<List<YoutubeRes>> getYoutubeList(String keyword) {
        List<YoutubeRes> videoList = wakeupSongClient.searchVideoByKeyword(keyword, 5).getItems().stream()
                .map(YoutubeApiUtil::getYoutubeRes).toList();
        return ResponseData.ok("유튜브 검색을 통한 기상송 조회 성공", videoList);
    }
}
