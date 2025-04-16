package b1nd.dodam.restapi;

import b1nd.dodam.process.listener.pushalarm.FcmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("fcm")
@Slf4j
public class FcmController {
    private final ApplicationEventPublisher eventPublisher;
    @PostMapping
    public void fcmTest(){
        eventPublisher.publishEvent(new FcmEvent("test","test","test"));
    }
}
