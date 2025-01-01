package b1nd.dodam.restapi.notice.application;

import b1nd.dodam.domain.rds.division.entity.DivisionMember;
import b1nd.dodam.domain.rds.division.service.DivisionMemberService;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.entity.NoticeDivision;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.domain.rds.notice.service.NoticeDivisionService;
import b1nd.dodam.domain.rds.notice.service.NoticeService;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.notice.application.data.req.GenerateNoticeReq;
import b1nd.dodam.restapi.notice.application.data.res.NoticeRes;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class NoticeUseCase {

    private final NoticeService noticeService;
    private final NoticeDivisionService noticeDivisionService;
    private final DivisionMemberService divisionMemberService;
    private final MemberAuthenticationHolder memberAuthenticationHolder;

    public ResponseData<Long> register(GenerateNoticeReq generateNoticeReq){
        Member member = memberAuthenticationHolder.current();
        Notice notice = noticeService.save(generateNoticeReq.toEntity(member));
        return ResponseData.of(HttpStatus.OK, "공지 생성 성공", notice.getId());
    }

    public ResponseData<List<NoticeRes>> getCreated(){
        Member member = memberAuthenticationHolder.current();
        List<DivisionMember> divisionMembers = divisionMemberService.getByMember(member);

        List<Notice> notices = divisionMembers.parallelStream()
                .map(DivisionMember::getDivision)
                .flatMap(division -> noticeDivisionService.getAllByDivision(division)
                        .stream())
                .map(NoticeDivision::getNotice)
                .toList();

        return ResponseData.of(HttpStatus.OK, "전체 공지 불러오기 성공", NoticeRes.of(notices, member));
    }

}
