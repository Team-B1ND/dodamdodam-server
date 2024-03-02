package b1nd.dodamcore.wakeupsong.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class YoutubeApiRes {

    private List<Item> items;

    @Getter
    public static class Item {
        String id;
        Snippet snippet;
        ContentDetails contentDetails;
        Thumbnails thumbnails;
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

    @Getter
    public static class ContentDetails {
        String duration;
        String dimension;
    }
}
