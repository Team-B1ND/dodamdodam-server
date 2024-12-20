package b1nd.dodam.restapi.support.exception;

import b1nd.dodam.discord.webhook.client.DiscordWebhookClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorNoticeSender {

    private final DiscordWebhookClient discordWebHookClient;
    private static final int MAX_LENGTH = 1000;

    @Async
    public void send(Exception e, RequestInfo request) {
        LocalDateTime now = LocalDateTime.now();
        String endpoint = getEndpoint(request.method(), request.uri(), request.query());
        String title = "🚨 OMG";
        String description = "### 🕖 Time\n"
                + now
                + "\n"
                + "### 🤔 Client\n"
                + request.remoteHost()
                + "\n"
                + "### 🔗 Endpoint\n"
                + endpoint
                + "\n"
                + "### 📄 Stack Trace\n"
                + "```\n"
                + getStackTrace(e)
                + "\n```";

        discordWebHookClient.notice("", title, description);
    }

    private String getEndpoint(String method, String uri, String query) {
        String endpoint = method + " " + uri;

        if (query != null) {
            endpoint += "?" + query;
        }

        return endpoint;
    }

    private String getStackTrace(Throwable th) {
        String stackTrace = Arrays.toString(th.getStackTrace());
        int length = Math.min(MAX_LENGTH, stackTrace.length());

        return stackTrace.substring(0, length);
    }

}

record RequestInfo(String method, String uri, String query, String remoteHost) {}
