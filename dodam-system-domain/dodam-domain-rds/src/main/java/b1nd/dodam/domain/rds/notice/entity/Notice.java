package b1nd.dodam.domain.rds.notice.entity;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Entity(name = "notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotNull
    private String title;

    @Setter
    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @Setter
    private NoticeStatus noticeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id")
    private Member member;

    @Builder
    public Notice(String title, String content, NoticeStatus noticeStatus, Member member) {
        this.title = title;
        this.content = content;
        this.noticeStatus = noticeStatus;
        this.member = member;
    }

    public void updateNotice(String title, String content){
        if (title != null){
            this.title = title;
        }
        if (content != null){
            this.content = content;
        }
    }

}
