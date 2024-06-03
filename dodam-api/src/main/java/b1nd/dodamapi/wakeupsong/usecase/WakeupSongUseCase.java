package b1nd.dodamapi.wakeupsong.usecase;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.common.util.YoutubeApiUtil;
import b1nd.dodamcore.member.application.MemberSessionHolder;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.wakeupsong.application.MusicChartClient;
import b1nd.dodamcore.wakeupsong.application.VideoClient;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class WakeupSongUseCase {
    private final WakeupSongService wakeupSongService;
    private final VideoClient videoClient;
    private final MusicChartClient chartClient;
    private final MemberSessionHolder memberSessionHolder;

    @Transactional(readOnly = true)
    public ResponseData<List<WakeupSongRes>> getAllowedWakeupSong(int year, int month, int day){
        List<WakeupSong> wakeupSongList = wakeupSongService.getByPlayDate(year, month, day);
        return ResponseData.ok("승인된 기상송 조회 성공", WakeupSongRes.of(wakeupSongList));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<WakeupSongRes>> getPendingWakeupSong(){
        List<WakeupSong> wakeupSongList = wakeupSongService.getPendingWakeupSong();
        return ResponseData.ok("승인 대기 중인 기상송 조회 성공", WakeupSongRes.of(wakeupSongList));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<WakeupSongRes>> getMyWakeupSong(){
        Member member = memberSessionHolder.current();
        List<WakeupSong> wakeupSongList = wakeupSongService.getMyWakeupSong(member);
        return ResponseData.ok("자신이 신청한 기상송 조회 성공", WakeupSongRes.of(wakeupSongList));
    }

    @Transactional(rollbackFor = Exception.class)
    public CompletableFuture<Response> createWakeupSong(String videoUrl) {
        Member member = verifyAlreadyAppliedFromSession();
        return CompletableFuture.supplyAsync(() -> {
            String videoId = YoutubeApiUtil.getVideoId(videoUrl);
            YoutubeApiRes.Snippet snippet = videoClient.getVideo(videoId).getItems().get(0).getSnippet();
            checkValidVideoType(snippet.getTitle());
            wakeupSongService.buildAndSaveWakeupSong(snippet, videoId, videoUrl, member);
            return Response.created("기상송 신청 성공");
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public CompletableFuture<Response> createWakeupSongByYoutubeSearch(ApplyWakeupSongBySearchReq req){
        Member member = verifyAlreadyAppliedFromSession();
        return CompletableFuture.supplyAsync(() -> {
            YoutubeApiRes.SearchItem searchItem = videoClient.searchVideoByKeyword(
                    req.title() + " " + req.artist(), 1).getItems().get(0);
            checkValidVideoType(searchItem.getSnippet().getTitle());
            wakeupSongService.buildAndSaveWakeupSong(searchItem.getSnippet(), searchItem.getId().getVideoId(),
                    "https://www.youtube.com/watch?v=" + searchItem.getId().getVideoId(), member);
            return Response.created("유튜브 검색을 통한 기상송 신청 성공");
        });
    }

    private void checkValidVideoType(String title){
        if(title.contains("MV")){
            throw new UnsupportedVideoTypeException();
        }
    }

    private Member verifyAlreadyAppliedFromSession(){
        Member member = memberSessionHolder.current();
        if (wakeupSongService.existsByMemberAndCreatedAt(member)){
            throw new WakeupSongAlreadyCreatedException();
        }
        return member;
    }

    @Transactional(rollbackFor = Exception.class)
    public Response allow(int id){
        WakeupSong wakeupSong = wakeupSongService.getById(id);
        wakeupSong.allow(memberSessionHolder.current());
        return Response.ok("기상송 승인 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response deny(int id){
        WakeupSong wakeupSong = wakeupSongService.getById(id);
        wakeupSong.deny();
        return Response.ok("기상송 거절 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response delete(int id){
        WakeupSong wakeupSong = wakeupSongService.getById(id);
        wakeupSongService.delete(wakeupSong);
        return Response.ok("기상송 삭제 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response deleteMyWakeupSong(int id) {
        WakeupSong wakeupSong = wakeupSongService.getById(id);
        wakeupSong.isApplicant(memberSessionHolder.current());
        wakeupSongService.delete(wakeupSong);
        return Response.ok("기상송 신청 취소 성공");
    }

    public ResponseData<CompletableFuture<List<ChartRes>>> getChartList() {
        return ResponseData.ok("차트 조회 성공", chartClient.getList());
    }

    public ResponseData<List<YoutubeRes>> getYoutubeList(String keyword) {
        List<YoutubeRes> videoList = videoClient.searchVideoByKeyword(keyword, 5).getItems().stream()
                .map(YoutubeApiUtil::getYoutubeRes).toList();
        return ResponseData.ok("유튜브 검색을 통한 기상송 조회 성공", videoList);
    }
}