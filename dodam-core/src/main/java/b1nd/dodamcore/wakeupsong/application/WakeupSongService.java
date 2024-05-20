package b1nd.dodamcore.wakeupsong.application;

import b1nd.dodamcore.common.util.HtmlConverter;
import b1nd.dodamcore.common.util.YoutubeApiUtil;
import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.member.application.MemberSessionHolder;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.wakeupsong.application.dto.req.ApplyWakeupSongBySearchReq;
import b1nd.dodamcore.wakeupsong.application.dto.res.ChartRes;
import b1nd.dodamcore.wakeupsong.application.dto.res.WakeupSongRes;
import b1nd.dodamcore.wakeupsong.application.dto.res.YoutubeApiRes;
import b1nd.dodamcore.wakeupsong.application.dto.res.YoutubeRes;
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

    public List<WakeupSong> getByPlayDate(int year, int month, int day) {
        return wakeupSongRepository.findAllByPlayAt(LocalDate.of(year, month, day));
    }

    public List<WakeupSong> getPendingWakeupSong() {
        return wakeupSongRepository.findAllByStatus(WakeupSongStatus.PENDING);
    }

    public List<WakeupSong> getMyWakeupSong(Member member) {
        return wakeupSongRepository.findAllByMember_IdAndStatus(member.getId(), WakeupSongStatus.PENDING);
    }

    private void save(WakeupSong wakeupSong) {
        wakeupSongRepository.save(wakeupSong);
    }

    @Transactional(rollbackFor = Exception.class)
    public void buildAndSaveWakeupSong(YoutubeApiRes.Snippet snippet, String videoId, String videoUrl, Member member){
        WakeupSong wakeupSong = buildWakeupSong(snippet, videoId, videoUrl, member);
        save(wakeupSong);
    }

    private WakeupSong buildWakeupSong(YoutubeApiRes.Snippet snippet, String videoId, String videoUrl, Member member){
        return WakeupSong.builder()
                .videoId(videoId)
                .videoTitle(HtmlConverter.of(snippet.getTitle()))
                .videoUrl(videoUrl)
                .channelTitle(snippet.getChannelTitle())
                .thumbnailUrl(YoutubeApiUtil.getThumbnailUrl(snippet).getUrl())
                .member(member)
                .build();
    }

    public Boolean existsByMemberAndCreatedAt(Member member){
        return wakeupSongRepository.existsByMember_IdAndCreatedAtAfter(member.getId(), ZonedDateTimeUtil.nowToLocalDateTime().minusDays(7));
    }

    public WakeupSong getById(int id){
        return wakeupSongRepository.findById(id)
                .orElseThrow(WakeupSongNotFoundException::new);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(WakeupSong wakeupSong){
        wakeupSongRepository.delete(wakeupSong);
    }
}
