package b1nd.dodam.domain.rds.notice.entity;

import b1nd.dodam.domain.rds.division.entity.DivisionMember;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "notice_division")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeDivision extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_notice_id")
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_division_id")
    private DivisionMember divisionMember;

    @Builder
    public NoticeDivision(Long id, Notice notice, DivisionMember divisionMember) {
        this.id = id;
        this.notice = notice;
        this.divisionMember = divisionMember;
    }

}
