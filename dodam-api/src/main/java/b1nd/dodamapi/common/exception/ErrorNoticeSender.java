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
        String requestPath = getRequestPath(request);
        String title = "🚨 OMG";
        String description = "### 🕖 발생 시간\n"
                + now
                + "\n"
                + "### 🔗 요청 URL\n"
                + requestPath
                + "\n"
                + "### 📄 Stack Trace\n"
                + "```\n"
                + getStackTrace(e)
                + "\n```";

        client.notice("", title, description);
    }

    private String getRequestPath(HttpServletRequest request) {
        String path = request.getMethod() + " " + request.getRequestURL();

        String queryString = request.getQueryString();
        if (queryString != null) {
            path += "?" + queryString;
        }

        return path;
    }

    private String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));

        return stringWriter.toString().substring(0, 1000);
    }

}
