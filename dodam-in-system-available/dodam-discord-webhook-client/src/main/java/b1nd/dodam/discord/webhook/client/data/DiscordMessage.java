package b1nd.dodam.discord.webhook.client.data;

import java.util.List;

public record DiscordMessage(String content, List<Embed> embeds) {
    public record Embed(String title, String description) {}
}
