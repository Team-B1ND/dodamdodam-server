package b1nd.dodamapi.wakeupsong.usecase;

import b1nd.dodamapi.common.util.YoutubeApiUtil;
import b1nd.dodamcore.common.exception.ExceptionCode;
import b1nd.dodamcore.common.exception.GlobalExceptionCode;
import b1nd.dodamapi.common.response.ErrorResponseEntity;
import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.common.util.HtmlConverter;
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
import b1nd.dodamcore.wakeupsong.domain.exception.WakeupSongExceptionCode;
import b1nd.dodamcore.wakeupsong.domain.exception.WakeupSongUrlMalformedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
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
            buildAndSaveWakeupSong(snippet, videoId,videoUrl,member);
            return Response.created("기상송 신청 성공");
        }).exceptionally(this::handleExceptionOnCreateWakeupSong);
    }

    @Transactional(rollbackFor = Exception.class)
    public CompletableFuture<Response> createWakeupSongByYoutubeSearch(ApplyWakeupSongBySearchReq req){
        Member member = verifyAlreadyAppliedFromSession();
        return CompletableFuture.supplyAsync(() -> {
            YoutubeApiRes.SearchItem searchItem = videoClient.searchVideoByKeyword(
                    req.title() + " " + req.artist(), 1).getItems().get(0);
            checkValidVideoType(searchItem.getSnippet().getTitle());
            buildAndSaveWakeupSong(searchItem.getSnippet(), searchItem.getId().getVideoId(),
                    "https://www.youtube.com/watch?v=" + searchItem.getId().getVideoId(), member);
            return Response.created("유튜브 검색을 통한 기상송 신청 성공");
        }).exceptionally(this::handleExceptionOnCreateWakeupSong);
    }

    private Response handleExceptionOnCreateWakeupSong(Throwable e) {
        Throwable cause = (e instanceof CompletionException) ? e.getCause() : e;
        ExceptionCode exceptionCode = mapExceptionToCodeOnCreateWakeupSong(cause);
        return ErrorResponseEntity.of(exceptionCode.getHttpStatus().value(), exceptionCode.getExceptionName(), exceptionCode.getMessage());
    }

    private ExceptionCode mapExceptionToCodeOnCreateWakeupSong(Throwable cause) {
        if (cause instanceof WakeupSongUrlMalformedException) {
            return WakeupSongExceptionCode.URL_MALFORMED;
        } else if (cause instanceof UnsupportedVideoTypeException) {
            return WakeupSongExceptionCode.UNSUPPORTED_TYPE;
        } else {
            return GlobalExceptionCode.INTERNAL_SERVER;
        }
    }

    private void buildAndSaveWakeupSong(YoutubeApiRes.Snippet snippet, String videoId, String videoUrl, Member member){
         WakeupSong wakeupSong = WakeupSong.builder()
                .videoId(videoId)
                .videoTitle(HtmlConverter.of(snippet.getTitle()))
                .videoUrl(videoUrl)
                .channelTitle(snippet.getChannelTitle())
                .thumbnailUrl(YoutubeApiUtil.getThumbnailUrl(snippet).getUrl())
                .member(member)
                .build();
        wakeupSongService.saveWakeupSong(wakeupSong);
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