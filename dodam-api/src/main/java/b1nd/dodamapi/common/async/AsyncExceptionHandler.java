package b1nd.dodamapi.common.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("message : " + ex.getMessage());
        log.error("method : " + method);
        for(Object param : params) {
            log.error("param : " + param);
        }
    }

}
