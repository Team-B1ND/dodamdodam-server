package b1nd.dodam.restapi.wakeupsong.application;

import b1nd.dodam.core.exception.ExceptionCode;
import b1nd.dodam.core.exception.global.GlobalExceptionCode;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.wakeupsong.entity.WakeupSong;
import b1nd.dodam.domain.rds.wakeupsong.exception.UnsupportedVideoTypeException;
import b1nd.dodam.domain.rds.wakeupsong.exception.WakeupSongAlreadyCreatedException;
import b1nd.dodam.domain.rds.wakeupsong.exception.WakeupSongExceptionCode;
import b1nd.dodam.domain.rds.wakeupsong.exception.WakeupSongUrlMalformedException;
import b1nd.dodam.domain.rds.wakeupsong.service.WakeupSongService;
import b1nd.dodam.melon.chart.client.MelonChartClient;
import b1nd.dodam.melon.chart.client.data.res.ChartRes;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.support.data.ErrorResponseEntity;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import b1nd.dodam.restapi.wakeupsong.application.data.req.ApplyWakeupSongBySearchReq;
import b1nd.dodam.restapi.wakeupsong.application.data.res.WakeupSongRes;
import b1nd.dodam.restapi.wakeupsong.application.support.HtmlConverter;
import b1nd.dodam.restapi.wakeupsong.application.support.YoutubeApiUtil;
import b1nd.dodam.youtube.video.client.YoutubeVideoClient;
import b1nd.dodam.youtube.video.client.data.YoutubeApiRes;
import b1nd.dodam.youtube.video.client.data.YoutubeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Component
@RequiredArgsConstructor
public class WakeupSongUseCase {

    private final WakeupSongService wakeupSongService;
    private final YoutubeVideoClient youtubeVideoClient;
    private final MelonChartClient melonChartClient;
    private final MemberAuthenticationHolder memberAuthenticationHolder;

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
        Member member = memberAuthenticationHolder.current();
        List<WakeupSong> wakeupSongList = wakeupSongService.getMyWakeupSong(member);
        return ResponseData.ok("자신이 신청한 기상송 조회 성공", WakeupSongRes.of(wakeupSongList));
    }

    @Transactional(rollbackFor = Exception.class)
    public CompletableFuture<Response> createWakeupSong(String videoUrl) {
        Member member = verifyAlreadyAppliedFromSession();
        String videoId = YoutubeApiUtil.getVideoId(videoUrl);

        return youtubeVideoClient.getVideo(videoId)
                .thenApply(videoResponse -> {
                    YoutubeApiRes.Snippet snippet = videoResponse.getItems().get(0).getSnippet();
                    checkValidVideoType(snippet.getTitle());
                    buildAndSaveWakeupSong(snippet, videoId, videoUrl, member);
                    return Response.created("기상송 신청 성공");
                }).exceptionally(this::handleExceptionOnCreateWakeupSong);
    }


    @Transactional(rollbackFor = Exception.class)
    public CompletableFuture<Response> createWakeupSongByYoutubeSearch(ApplyWakeupSongBySearchReq req){
        Member member = verifyAlreadyAppliedFromSession();
        return CompletableFuture.supplyAsync(() -> {
            YoutubeApiRes.SearchItem searchItem = youtubeVideoClient.searchVideoByKeyword(
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
        return ErrorResponseEntity.of(exceptionCode.getStatus(), exceptionCode.getExceptionName(), exceptionCode.getMessage());
    }

    private ExceptionCode mapExceptionToCodeOnCreateWakeupSong(Throwable cause) {
        if (cause instanceof WakeupSongUrlMalformedException) {
            return WakeupSongExceptionCode.URL_MALFORMED;
        } else if (cause instanceof UnsupportedVideoTypeException) {
            return WakeupSongExceptionCode.UNSUPPORTED_TYPE;
        } else if(cause instanceof WakeupSongAlreadyCreatedException){
            return WakeupSongExceptionCode.ALREADY_APPLIED;
        }else {
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
        Member member = memberAuthenticationHolder.current();
        if (wakeupSongService.existsByMemberAndCreatedAt(member)){
            throw new WakeupSongAlreadyCreatedException();
        }
        return member;
    }

    @Transactional(rollbackFor = Exception.class)
    public Response allow(int id){
        WakeupSong wakeupSong = wakeupSongService.getById(id);
        wakeupSong.allow(memberAuthenticationHolder.current());
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
        wakeupSong.isApplicant(memberAuthenticationHolder.current());
        wakeupSongService.delete(wakeupSong);
        return Response.ok("기상송 신청 취소 성공");
    }

    @Transactional(readOnly = true)
    public CompletableFuture<ResponseData<List<ChartRes>>> getChartList() {
        return melonChartClient.getList().thenApply(res ->  ResponseData.ok("차트 조회 성공", res));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<YoutubeRes>> getYoutubeList(String keyword) {
        List<YoutubeRes> videoList = youtubeVideoClient.searchVideoByKeyword(keyword, 5).getItems().stream()
                .map(YoutubeApiUtil::getYoutubeRes).toList();
        return ResponseData.ok("유튜브 검색을 통한 기상송 조회 성공", videoList);
    }

}
