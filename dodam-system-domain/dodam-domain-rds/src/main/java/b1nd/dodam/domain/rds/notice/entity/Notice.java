package b1nd.dodam.domain.rds.notice.entity;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import b1nd.dodam.domain.rds.support.enumeration.FileType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    private String fileUrl;

    private FileType fileType;

    private NoticeStatus noticeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id")
    private Member member;

    @Builder
    public Notice(Long id, String title, String content, String fileUrl, FileType fileType, NoticeStatus noticeStatus, Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.noticeStatus = noticeStatus;
        this.member = member;
    }
}
