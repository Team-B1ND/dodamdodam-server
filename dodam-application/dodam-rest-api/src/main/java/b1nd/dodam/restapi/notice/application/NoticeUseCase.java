package b1nd.dodam.restapi.notice.application;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.service.NoticeService;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.notice.application.data.req.GenerateNoticeReq;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class NoticeUseCase {

    private final NoticeService noticeService;
    private final MemberAuthenticationHolder authHolder;

    public ResponseData register(GenerateNoticeReq generateNoticeReq){
        Member member = authHolder.current();
        Notice notice = noticeService.save(generateNoticeReq.toEntity(member));
        return ResponseData.of(HttpStatus.OK, "공지 생성 성공", notice.getId());
    }

}
