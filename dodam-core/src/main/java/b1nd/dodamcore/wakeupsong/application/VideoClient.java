package b1nd.dodamcore.wakeupsong.application;

import b1nd.dodamcore.wakeupsong.application.dto.res.YoutubeApiRes;

public interface VideoClient {

    YoutubeApiRes.Video getVideo(String videoId);

    YoutubeApiRes.Search searchVideoByKeyword(String keyword, int size);
}
