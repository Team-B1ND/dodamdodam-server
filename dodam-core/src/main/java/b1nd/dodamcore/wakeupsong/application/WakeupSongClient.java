package b1nd.dodamcore.wakeupsong.application;

import b1nd.dodamcore.wakeupsong.application.dto.res.YoutubeApiRes;
import b1nd.dodamcore.wakeupsong.application.dto.res.YoutubeSearchApiRes;

public interface WakeupSongClient {

    YoutubeApiRes getVideo(String videoId);

    YoutubeSearchApiRes searchVideoByKeyword(String keyword, int size);
}
