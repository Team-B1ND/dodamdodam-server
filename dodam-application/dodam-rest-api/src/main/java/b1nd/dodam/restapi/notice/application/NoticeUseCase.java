package b1nd.dodam.restapi.notice.application;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.division.entity.DivisionMember;
import b1nd.dodam.domain.rds.division.service.DivisionMemberService;
import b1nd.dodam.domain.rds.division.service.DivisionService;
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

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class NoticeUseCase {

    private final NoticeService noticeService;
    private final NoticeDivisionService noticeDivisionService;
    private final DivisionService divisionService;
    private final DivisionMemberService divisionMemberService;
    private final MemberAuthenticationHolder memberAuthenticationHolder;

    public ResponseData<Long> register(GenerateNoticeReq generateNoticeReq){
        Member member = memberAuthenticationHolder.current();
        Notice notice = noticeService.save(generateNoticeReq.toEntity(member));

        List<Division> divisions = generateNoticeReq.divisions().stream()
                .map(divisionService::getById)
                .toList();

        List<NoticeDivision> noticeDivisions = generateNoticeReq.toEntity(notice, divisions);

        noticeService.changeStatus(notice.getId(), NoticeStatus.CREATED);
        noticeDivisionService.saveAll(noticeDivisions);

        return ResponseData.of(HttpStatus.OK, "공지 생성 성공", notice.getId());
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NoticeRes>> getNotices(Long lastId, int limit, NoticeStatus status) {
        Member member = memberAuthenticationHolder.current();
        List<DivisionMember> divisionMembers = divisionMemberService.getByMember(member);

        List<Long> divisionIds = divisionMembers.stream()
                .map(divisionMember -> divisionMember.getDivision().getId())
                .toList();

        List<Notice> notices = noticeService.getAllByStatus(divisionIds, status, lastId, limit);

        return ResponseData.of(HttpStatus.OK, "전체 공지 불러오기 성공", NoticeRes.of(notices, member));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NoticeRes>> getNoticesByDivision(Long divisionId, Long lastId, int limit){
        Member member = memberAuthenticationHolder.current();
        Division division = divisionService.getById(divisionId);

        List<Notice> notices = noticeService.getAllByDivision(member.getId(), division.getId(), lastId, limit)
                .stream()
                .toList();

        return ResponseData.of(HttpStatus.OK, "카테고리별 공지 불러오기 성공", NoticeRes.of(notices, member));
    }

}
