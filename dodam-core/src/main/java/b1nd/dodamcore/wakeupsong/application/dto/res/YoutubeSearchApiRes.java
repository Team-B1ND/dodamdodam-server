package b1nd.dodamcore.wakeupsong.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class YoutubeSearchApiRes {

    private List<Item> items;

    @Getter
    public static class Item {
        Id id;
    }

    @Getter
    public static class Id {
        String videoId;
    }
}
