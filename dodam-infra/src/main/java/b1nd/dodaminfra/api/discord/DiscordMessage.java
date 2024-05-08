package b1nd.dodaminfra.api.discord;

import java.util.List;

record DiscordMessage(String content, List<Embed> embeds) {}

record Embed(String title, String description) {}
