package b1nd.dodaminfra.api.youtube;

import b1nd.dodamcore.wakeupsong.application.WakeupSongClient;
import b1nd.dodamcore.wakeupsong.application.dto.res.YoutubeApiRes;
import b1nd.dodamcore.wakeupsong.application.dto.res.YoutubeSearchApiRes;
import b1nd.dodaminfra.webclient.WebClientSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class YoutubeClient implements WakeupSongClient {

    private final YoutubeProperties youtubeProperties;
    private final WebClientSupport webClientSupport;

    @Override
    public YoutubeApiRes getVideo(String videoId) {
        return webClientSupport.get(
                UriComponentsBuilder.fromUriString(youtubeProperties.url().getVideo())
                        .build(youtubeProperties.getApiKey(), videoId).toString(),
                YoutubeApiRes.class
        ).block();
    }

    @Override
    public YoutubeSearchApiRes searchVideoByKeyword(String title, String artist) {
        return webClientSupport.get(
                UriComponentsBuilder.fromUriString(youtubeProperties.url().getSearch())
                        .build(youtubeProperties.getApiKey(), title + " " + artist).toString(),
                YoutubeSearchApiRes.class
        ).block();
    }
}
