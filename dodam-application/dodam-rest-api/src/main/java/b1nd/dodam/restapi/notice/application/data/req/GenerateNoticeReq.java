package b1nd.dodam.restapi.notice.application.data.req;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.entity.NoticeDivision;
import b1nd.dodam.domain.rds.notice.entity.NoticeFile;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.restapi.notice.application.data.File;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

import static b1nd.dodam.domain.rds.notice.enumration.NoticeStatus.DRAFT;

public record GenerateNoticeReq (@NotEmpty String title, @NotEmpty String content, List<File> files,
                                 List<Long> divisions){

    public Notice toEntity(Member member) {
        if (title == null || content == null) {
            throw new IllegalArgumentException("Title or content cannot be null");
        }

        Notice notice = Notice.builder()
                .title(title)
                .content(content)
                .noticeStatus(NoticeStatus.DRAFT)
                .member(member)
                .build();

        if (files != null && !files.isEmpty()) {
            files.forEach(file -> notice.addNoticeFiles(
                    NoticeFile.builder()
                            .fileUrl(file.url())
                            .fileName(file.name())
                            .fileType(file.fileType())
                            .build()
            ));
        }

        return notice;
    }

    public List<NoticeDivision> toEntity(Notice notice, List<Division> divisions) {
        if (divisions == null || divisions.isEmpty()) {
            throw new IllegalArgumentException("Divisions cannot be null or empty");
        }

        return divisions.stream()
                .map(division -> {
                    NoticeDivision noticeDivision = GenerateNoticeReq.toEntity(notice, division);
                    notice.addNoticeDivision(noticeDivision);
                    return noticeDivision;
                })
                .collect(Collectors.toList());
    }

    public static NoticeDivision toEntity(Notice notice, Division division){
        if (notice == null || division == null) {
            throw new IllegalArgumentException("Notice or Division cannot be null");
        }

        return NoticeDivision.builder()
                .notice(notice)
                .division(division)
                .build();
    }
}