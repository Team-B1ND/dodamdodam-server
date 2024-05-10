package b1nd.dodamapi.common.exception;

import b1nd.dodamcore.notice.NoticeClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ErrorNoticeSender {

    private final NoticeClient client;

    @Async
    public void send(Exception e, HttpServletRequest request) {
        LocalDateTime now = LocalDateTime.now();
        String clientInfo = getClientInfo(request);
        String endpoint = getEndpoint(request);
        String title = "🚨 OMG";
        String description = "### 🕖 Time\n"
                + now
                + "\n"
                + "### 🤔 Client"
                + clientInfo
                + "### 🔗 Endpoint\n"
                + endpoint
                + "\n"
                + "### 📄 Stack Trace\n"
                + "```\n"
                + getStackTrace(e)
                + "\n```";

        client.notice("", title, description);
    }

    private String getClientInfo(HttpServletRequest request) {
        return request.getRemoteHost();
    }

    private String getEndpoint(HttpServletRequest request) {
        String endpoint = request.getMethod() + " " + request.getRequestURI();

        String queryString = request.getQueryString();
        if (queryString != null) {
            endpoint += "?" + queryString;
        }

        return endpoint;
    }

    private String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));

        return stringWriter.toString().substring(0, 1000);
    }

}
