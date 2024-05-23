package b1nd.dodaminfra.scheduler;

import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.nightstudy.application.NightStudyService;
import b1nd.dodamcore.nightstudy.domain.entity.NightStudy;
import b1nd.dodamcore.nightstudy.domain.enums.NightStudyStatus;
import b1nd.dodaminfra.cloud.fcm.FirebaseMessageSender;
import b1nd.dodaminfra.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final RedisUtil redisUtil;
    private final FirebaseMessageSender messageSender;
    private final NightStudyService nightStudyService;

    @Scheduled(cron = "0 0 7 1 * ?", zone = "Asia/Seoul")
    public void flushAllMealData() {
        try {
            log.info("<<<<<<<<<< flushMealData Start {} >>>>>>>>>>", ZonedDateTime.now());
            redisUtil.evictAllCacheValues();
        } catch (Exception e) {
            log.error("fail to process", e);
        }
    }

    @Scheduled(cron = "0 0 13 * * ?", zone = "Asia/Seoul")
    public void notifyEndNightStudy(){
        LocalDate now = ZonedDateTimeUtil.nowToLocalDate();
        List<NightStudy> nightStudies = nightStudyService.getEndOnToday(now);
        List<Member> members = nightStudies.parallelStream()
                .filter(n -> n.getStatus() == NightStudyStatus.ALLOWED)
                .map(n -> n.getStudent().getMember())
                .toList();
        messageSender.sendToMemberList(members,"도담도담","심야자습 신청이 오늘 끝났습니다.\n연장을 원하시면 다시 신청해주세요.");
    }
}
