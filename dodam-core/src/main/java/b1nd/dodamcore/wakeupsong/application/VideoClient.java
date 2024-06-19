package b1nd.dodamcore.wakeupsong.application;

import b1nd.dodamcore.wakeupsong.application.dto.res.YoutubeApiRes;

import java.util.concurrent.CompletableFuture;

public interface VideoClient {

    CompletableFuture<YoutubeApiRes.Video> getVideo(String videoId);

    YoutubeApiRes.Search searchVideoByKeyword(String keyword, int size);
}
