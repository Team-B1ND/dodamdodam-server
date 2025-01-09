package b1nd.dodam.domain.rds.notice.entity;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.domain.rds.recruitment.entity.RecruitmentFile;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import b1nd.dodam.domain.rds.support.enumeration.FileType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeFile> noticeFiles = new ArrayList<>();

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeDivision> noticeDivisions = new ArrayList<>();

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

    public void setNoticeStatus(NoticeStatus noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    public void addNoticeFiles(NoticeFile noticeFile) {
        this.noticeFiles.add(noticeFile);
        noticeFile.setNotice(this);
    }

    public void addNoticeDivision(NoticeDivision noticeDivision){
        this.noticeDivisions.add(noticeDivision);
        noticeDivision.setNotice(this);
    }

}
