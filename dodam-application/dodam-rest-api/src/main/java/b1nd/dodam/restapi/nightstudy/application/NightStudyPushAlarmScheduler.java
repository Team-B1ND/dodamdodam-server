package b1nd.dodam.restapi.nightstudy.application;

import b1nd.dodam.domain.rds.nightstudy.entity.NightStudy;
import b1nd.dodam.domain.rds.nightstudy.service.NightStudyService;
import b1nd.dodam.firebase.client.FCMClient;
import b1nd.dodam.process.listener.pushalarm.FcmEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NightStudyPushAlarmScheduler {
    private final NightStudyService service;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "0 0 8 * * ?")
    public void scheduledPushAlarm() {
        List<NightStudy> nightStudies = service.getByEndDate(LocalDate.now().minusDays(1));
        List<String> tokens = nightStudies.stream()
                .map(n -> n.getStudent().getMember().getPushToken())
                .toList();
        tokens.parallelStream().forEach(token ->
                    eventPublisher.publishEvent(new FcmEvent(token, "심야자습 만료", "심야자습이 만료됐어요."))
        );
    }
}
