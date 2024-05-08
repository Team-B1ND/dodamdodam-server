package b1nd.dodaminfra.api.discord;

import b1nd.dodamcore.notice.NoticeClient;
import b1nd.dodaminfra.webclient.WebClientSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
final class DiscordClient implements NoticeClient {

    private final WebClientSupport webClient;
    private final DiscordProperties properties;

    @Override
    public void notice(String content, String title, String description) {
        webClient.post(
                properties.getUrl(),
                new DiscordMessage(content, List.of(new Embed(title, description)))
        );
    }

}
