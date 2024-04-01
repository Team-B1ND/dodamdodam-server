package b1nd.dodamcore.wakeupsong.application.dto.res;

import b1nd.dodamcore.wakeupsong.domain.entity.WakeupSong;
import b1nd.dodamcore.wakeupsong.domain.enums.WakeupSongStatus;

import java.time.LocalDate;
import java.util.List;

public record WakeupSongRes(int id,
                            String thumbnail,
                            String videoTitle, String videoId, String videoUrl,
                            String channelTitle,
                            WakeupSongStatus status,
                            LocalDate createdAt) {
    public static List<WakeupSongRes> of(List<WakeupSong> wakeupSongs) {
        return wakeupSongs.stream()
                .map(WakeupSongRes::of)
                .toList();
    }

    public static WakeupSongRes of(WakeupSong wakeupSong) {
        return new WakeupSongRes(
                wakeupSong.getId(),
                wakeupSong.getThumbnailUrl(),
                wakeupSong.getVideoTitle(), wakeupSong.getVideoId(), wakeupSong.getVideoUrl(),
                wakeupSong.getChannelTitle(),
                wakeupSong.getStatus(),
                wakeupSong.getCreatedAt().toLocalDate()
        );
    }
}