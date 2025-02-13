package b1nd.dodam.restapi.notice.application.data.res;

import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.entity.NoticeFile;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.restapi.member.application.data.res.MemberInfoRes;

import java.time.LocalDateTime;
import java.util.List;

public record NoticeRes(
        Long id,
        String title,
        String content,
        NoticeStatus noticeStatus,
        List<NoticeFileRes> noticeFileRes,
        MemberInfoRes memberInfoRes,
        LocalDateTime createdAt, LocalDateTime modifiedAt
) {
    public static List<NoticeRes> of(List<Notice> notices, List<NoticeFile> files) {
        return notices.stream()
                .map(notice -> NoticeRes.of(notice, files))
                .toList();
    }

    public static NoticeRes of(Notice notice, List<NoticeFile> files) {
        return new NoticeRes(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getNoticeStatus(),
                NoticeFileRes.of(files),
                MemberInfoRes.of(notice.getMember(), null, null),
                notice.getCreatedAt(),
                notice.getModifiedAt()
        );
    }

}
