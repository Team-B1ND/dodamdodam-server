package b1nd.dodam.restapi.notice.application.data.res;

import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.domain.rds.support.enumeration.FileType;
import b1nd.dodam.restapi.member.application.data.res.MemberInfoRes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record NoticeRes(
        Long id,
        String title,
        String content,
        String fileUrl,
        FileType fileType,
        NoticeStatus noticeStatus,
        MemberInfoRes memberInfoRes,
        LocalDateTime createdAt, LocalDateTime modifiedAt
) {
    public static List<NoticeRes> of(List<Notice> notices) {
        return notices.stream()
                .map(NoticeRes::of)
                .toList();
    }

    public static NoticeRes of(Notice notice) {
        return new NoticeRes(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getFileUrl(),
                notice.getFileType(),
                notice.getNoticeStatus(),
                MemberInfoRes.of(notice.getMember(), null, null),
                notice.getCreatedAt(),
                notice.getModifiedAt()
        );
    }
}
