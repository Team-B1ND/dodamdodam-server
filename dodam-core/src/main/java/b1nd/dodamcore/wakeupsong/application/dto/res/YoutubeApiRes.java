package b1nd.dodamcore.wakeupsong.application.dto.res;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class YoutubeApiRes {

    @Getter
    public static class Video {
        private List<VideoItem> items;
    }

    @Getter
    public static class Search {
        private List<SearchItem> items;
    }

    @Getter
    public static class VideoItem {
        String id;
        Snippet snippet;
    }

    @Getter
    public static class SearchItem {
        Id id;
        Snippet snippet;
    }

    @Getter
    public static class Id {
        String videoId;
    }

    @Getter
    public static class Snippet {
        String channelTitle;
        String title;
        Thumbnails thumbnails;
    }

    @Getter
    public static class Thumbnails {
        Thumbnail medium;
        Thumbnail high;
        Thumbnail standard;
        Thumbnail maxres;
    }

    @Getter
    public static class Thumbnail {
        String url;
        int width;
        int height;
    }
}
