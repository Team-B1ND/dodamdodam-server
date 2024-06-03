package b1nd.dodamcore.common.util;

import b1nd.dodamcore.wakeupsong.application.dto.res.YoutubeApiRes;
import b1nd.dodamcore.wakeupsong.application.dto.res.YoutubeRes;
import b1nd.dodamcore.wakeupsong.domain.exception.WakeupSongUrlMalformedException;

import java.util.Optional;

public final class YoutubeApiUtil {

    private YoutubeApiUtil() {
    }

    static public YoutubeApiRes.Thumbnail getThumbnailUrl(YoutubeApiRes.Snippet snippet) {
        Optional<YoutubeApiRes.Thumbnail> standard = Optional.ofNullable(snippet.getThumbnails().getStandard());
        return standard.orElseGet(() -> snippet.getThumbnails().getHigh());
    }

    static public String getVideoId(String videoUrl){
        try {
            return videoUrl.split("/?v=")[1].split("&")[0];
        } catch (IndexOutOfBoundsException e) {
            throw new WakeupSongUrlMalformedException();
        }
    }

    static public YoutubeRes getYoutubeRes(YoutubeApiRes.SearchItem item) {
        return new YoutubeRes(
                HtmlConverter.of(item.getSnippet().getTitle()),
                item.getId().getVideoId(),
                "https://www.youtube.com/watch?v=" + item.getId().getVideoId(),
                item.getSnippet().getChannelTitle(),
                YoutubeApiUtil.getThumbnailUrl(item.getSnippet()).getUrl()
        );
    }
}
