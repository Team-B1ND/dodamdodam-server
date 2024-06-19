package b1nd.dodaminfra.api.youtube;

import b1nd.dodamcore.wakeupsong.application.VideoClient;
import b1nd.dodamcore.wakeupsong.application.dto.res.YoutubeApiRes;
import b1nd.dodaminfra.webclient.WebClientSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class YoutubeClient implements VideoClient {

    private final YoutubeProperties youtubeProperties;
    private final WebClientSupport webClientSupport;

    @Override
    public CompletableFuture<YoutubeApiRes.Video> getVideo(String videoId) {
        return webClientSupport.get(
                UriComponentsBuilder.fromUriString(youtubeProperties.url().getVideo())
                        .build(youtubeProperties.getApiKey(), videoId).toString(),
                YoutubeApiRes.Video.class
        ).toFuture();
    }

    @Override
    public YoutubeApiRes.Search searchVideoByKeyword(String keyword, int size) {
        return webClientSupport.get(
                UriComponentsBuilder.fromUriString(youtubeProperties.url().getSearch())
                        .build(youtubeProperties.getApiKey(), keyword, size).toString(),
                YoutubeApiRes.Search.class
        ).block();
    }
}
