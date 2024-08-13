package b1nd.dodam.youtube.video.client;

import b1nd.dodam.client.core.WebClientSupport;
import b1nd.dodam.youtube.video.client.data.YoutubeApiRes;
import b1nd.dodam.youtube.video.client.properties.YoutubeProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class YoutubeVideoClient {

    private final YoutubeProperties youtubeProperties;
    private final WebClientSupport webClientSupport;

    public CompletableFuture<YoutubeApiRes.Video> getVideo(String videoId) {
        return webClientSupport.get(
                UriComponentsBuilder.fromUriString(youtubeProperties.url().getVideo())
                        .build(youtubeProperties.getApiKey(), videoId).toString(),
                YoutubeApiRes.Video.class
        ).toFuture();
    }

    public YoutubeApiRes.Search searchVideoByKeyword(String keyword, int size) {
        return webClientSupport.get(
                UriComponentsBuilder.fromUriString(youtubeProperties.url().getSearch())
                        .build(youtubeProperties.getApiKey(), keyword, size).toString(),
                YoutubeApiRes.Search.class
        ).block();
    }

}
