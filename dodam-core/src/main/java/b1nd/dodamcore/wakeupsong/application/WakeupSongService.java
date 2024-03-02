package b1nd.dodamcore.wakeupsong.application;

import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.member.application.MemberSessionHolder;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.wakeupsong.application.dto.req.ApplyWakeupSongBySearchReq;
import b1nd.dodamcore.wakeupsong.application.dto.res.ChartRes;
import b1nd.dodamcore.wakeupsong.application.dto.res.WakeupSongRes;
import b1nd.dodamcore.wakeupsong.application.dto.res.YoutubeApiRes;
import b1nd.dodamcore.wakeupsong.domain.entity.WakeupSong;
import b1nd.dodamcore.wakeupsong.domain.enums.WakeupSongStatus;
import b1nd.dodamcore.wakeupsong.domain.exception.WakeupSongAlreadyCreatedException;
import b1nd.dodamcore.wakeupsong.domain.exception.WakeupSongNotFoundException;
import b1nd.dodamcore.wakeupsong.domain.exception.WakeupSongUrlMalformedException;
import b1nd.dodamcore.wakeupsong.repository.WakeupSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WakeupSongService {

    private final WakeupSongRepository wakeupSongRepository;
    private final WakeupSongClient wakeupSongClient;
    private final ChartClient chartClient;
    private final MemberSessionHolder memberSessionHolder;

    public List<WakeupSongRes> getAllowedWakeupSongByPlayDate(int year, int month, int day) {
        return WakeupSongRes.of(
                wakeupSongRepository.findAllByPlayAt(LocalDate.of(year, month, day))
        );
    }

    public List<WakeupSongRes> getPendingWakeupSong() {
        return WakeupSongRes.of(
                wakeupSongRepository.findAllByStatus(WakeupSongStatus.PENDING)
        );
    }

    public List<WakeupSongRes> getMyWakeupSong() {
        return WakeupSongRes.of(
                wakeupSongRepository.findAllByMember_IdAndStatus(memberSessionHolder.current().getId(), WakeupSongStatus.PENDING)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void createWakeupSong(String videoUrl) {

        Member member = memberSessionHolder.current();

        if (wakeupSongRepository.existsByMember_IdAndCreatedAtAfter(member.getId(), ZonedDateTimeUtil.nowToLocalDateTime().minusDays(7))) {
            throw new WakeupSongAlreadyCreatedException();
        }

        String videoId;
        try {
            videoId = videoUrl.split("/?v=")[1].split("&")[0];
        } catch (IndexOutOfBoundsException e) {
            throw new WakeupSongUrlMalformedException();
        }

        YoutubeApiRes.Snippet snippet = wakeupSongClient.getVideo(videoId).getItems().get(0).getSnippet();

        WakeupSong wakeupSong = WakeupSong.builder()
                .videoId(videoId)
                .videoTitle(snippet.getTitle())
                .videoUrl(videoUrl)
                .channelTitle(snippet.getChannelTitle())
                .thumbnailUrl(getThumbnailUrl(snippet).getUrl())
                .member(member)
                .build();

        wakeupSongRepository.save(wakeupSong);
    }

    private YoutubeApiRes.Thumbnail getThumbnailUrl(YoutubeApiRes.Snippet snippet) {
        Optional<YoutubeApiRes.Thumbnail> standard = Optional.ofNullable(snippet.getThumbnails().getStandard());
        return standard.orElseGet(() -> snippet.getThumbnails().getHigh());
    }

    @Transactional(rollbackFor = Exception.class)
    public void createWakeupSongByYoutubeSearch(ApplyWakeupSongBySearchReq req) {
        String videoId = wakeupSongClient.searchVideoByKeyword(req.title(), req.artist()).getItems().get(0).getId().getVideoId();
        createWakeupSong("https://www.youtube.com/watch?v=" + videoId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void allowWakeupSong(int id) {
        WakeupSong wakeupSong = wakeupSongRepository.findById(id)
                .orElseThrow(WakeupSongNotFoundException::new);

        wakeupSong.allow(memberSessionHolder.current());
    }

    @Transactional(rollbackFor = Exception.class)
    public void denyWakeupSong(int id) {
        WakeupSong wakeupSong = wakeupSongRepository.findById(id)
                .orElseThrow(WakeupSongNotFoundException::new);

        wakeupSong.deny();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteWakeupSong(int id) {
        WakeupSong wakeupSong = wakeupSongRepository.findById(id)
                .orElseThrow(WakeupSongNotFoundException::new);

        wakeupSongRepository.delete(wakeupSong);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteMyWakeupSong(int id) {
        WakeupSong wakeupSong = wakeupSongRepository.findById(id)
                .orElseThrow(WakeupSongNotFoundException::new);

        wakeupSong.isApplicant(memberSessionHolder.current());

        wakeupSongRepository.delete(wakeupSong);
    }

    public List<ChartRes> getChartList() {
        return chartClient.getList().join();
    }
}
