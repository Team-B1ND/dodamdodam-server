package b1nd.dodam.discord.webhook.client;

import b1nd.dodam.client.core.WebClientSupport;
import b1nd.dodam.discord.webhook.client.data.DiscordMessage;
import b1nd.dodam.discord.webhook.client.properties.DiscordProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class DiscordWebhookClient {

    private final WebClientSupport webClient;
    private final DiscordProperties properties;

    public void notice(String content, String title, String description) {
        webClient.post(
                properties.getUrl(),
                new DiscordMessage(content, List.of(new DiscordMessage.Embed(title, description)))
        );
    }

}
