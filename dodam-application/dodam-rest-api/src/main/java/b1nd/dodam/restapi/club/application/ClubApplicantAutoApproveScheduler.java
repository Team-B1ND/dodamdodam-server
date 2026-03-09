package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.entity.ClubTime;
import b1nd.dodam.domain.rds.club.enumeration.ClubTimeType;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.domain.rds.club.service.ClubService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClubApplicantAutoApproveScheduler {
    private final ClubService clubService;
    private final ClubMemberService clubMemberService;
    private final TaskScheduler taskScheduler;

    private ScheduledFuture<?> scheduledTask;

    @PostConstruct
    public void init() {
        scheduleAutoApproval();
    }

    public void scheduleAutoApproval() {
        cancelExisting();
        try {
            ClubTime applicantTime = clubService.getClubTime(ClubTimeType.CLUB_APPLICANT);
            LocalDateTime endTime = applicantTime.getEnd();
            LocalDateTime now = LocalDateTime.now();

            if (endTime.isAfter(now)) {
                scheduledTask = taskScheduler.schedule(
                        this::executeAutoApproval,
                        endTime.atZone(ZoneId.of("Asia/Seoul")).toInstant()
                );
                log.info("동아리 자동 승인 스케줄 등록: {}", endTime);
            } else {
                executeAutoApproval();
            }
        } catch (Exception e) {
            log.info("동아리 신청 기간이 설정되지 않아 자동 승인 스케줄을 등록하지 않습니다.");
        }
    }

    @Transactional
    public void executeAutoApproval() {
        log.info("동아리 자동 승인 실행");
        clubMemberService.autoApproveClubsWithinLimit();
    }

    private void cancelExisting() {
        if (scheduledTask != null && !scheduledTask.isDone()) {
            scheduledTask.cancel(false);
        }
    }
}
