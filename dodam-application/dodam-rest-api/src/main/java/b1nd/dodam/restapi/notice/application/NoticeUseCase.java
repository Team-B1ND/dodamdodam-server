package b1nd.dodam.restapi.notice.application;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.division.service.DivisionMemberService;
import b1nd.dodam.domain.rds.division.service.DivisionService;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.entity.NoticeDivision;
import b1nd.dodam.domain.rds.notice.entity.NoticeFile;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.domain.rds.notice.service.NoticeService;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.notice.application.data.req.GenerateNoticeReq;
import b1nd.dodam.restapi.notice.application.data.req.ModifyNoticeReq;
import b1nd.dodam.restapi.notice.application.data.res.NoticeRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class NoticeUseCase {

    private final NoticeService noticeService;
    private final DivisionService divisionService;
    private final DivisionMemberService divisionMemberService;
    private final MemberAuthenticationHolder memberAuthenticationHolder;

    public ResponseData<Long> register(GenerateNoticeReq generateNoticeReq) {
        Member member = memberAuthenticationHolder.current();
        Notice notice = noticeService.save(generateNoticeReq.toEntity(member));
        List<Division> divisions = divisionService.getAllByIds(generateNoticeReq.divisions());
        saveFilesAndDivisions(generateNoticeReq.toNoticeFiles(notice), generateNoticeReq.toNoticeDivisions(notice, divisions));
        return ResponseData.of(HttpStatus.OK, "공지 생성 성공", notice.getId());
    }

    private void saveFilesAndDivisions(List<NoticeFile> noticeFiles, List<NoticeDivision> noticeDivisions){
        noticeService.saveAllNoticeFiles(noticeFiles);
        noticeService.saveAll(noticeDivisions);
    }

    public Response modify(Long id, ModifyNoticeReq modifyNoticeReq){
        Member member = memberAuthenticationHolder.current();
        noticeService.updateNotice(id, member, modifyNoticeReq.title(), modifyNoticeReq.content());
        return Response.ok("공지 수정 성공");
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NoticeRes>> getNotices(String keyword, Long lastId, int limit) {
        Member member = memberAuthenticationHolder.current();
        List<Long> divisionIds = divisionMemberService.getIdsByMember(member);
        List<Notice> notices = noticeService.getAllByStatus(keyword, divisionIds, lastId, limit);
        List<NoticeRes> noticeResList = convertToNoticeRes(notices);
        return ResponseData.of(HttpStatus.OK, "전체 공지 불러오기 성공", noticeResList);
    }

    @Transactional(readOnly = true)
    public ResponseData<List<NoticeRes>> getNoticesByDivision(Long divisionId, Long lastId, int limit) {
        Member member = memberAuthenticationHolder.current();
        List<Notice> notices = noticeService.getAllByDivision(member.getId(), divisionId, lastId, limit);
        List<NoticeRes> noticeResList = convertToNoticeRes(notices);
        return ResponseData.of(HttpStatus.OK, "카테고리별 공지 불러오기 성공", noticeResList);
    }

    public Response deleteNotice(Long id){
        Member member = memberAuthenticationHolder.current();
        noticeService.changeStatus(id, member, NoticeStatus.DELETED);
         return Response.ok("공지 삭제 성공");
    }

    private List<NoticeRes> convertToNoticeRes(List<Notice> notices) {
        Map<Long, List<NoticeFile>> noticeFileMap = noticeService.getNoticeFileMap(notices);
        return notices.stream()
                .map(notice -> NoticeRes.of(notice, noticeFileMap.getOrDefault(notice.getId(), List.of())))
                .collect(Collectors.toList());
    }

}
