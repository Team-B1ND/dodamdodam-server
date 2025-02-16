package b1nd.dodam.domain.rds.notice.entity;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity(name = "notice_division")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeDivision extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_notice_id")
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_division_id")
    private Division division;

    @Builder
    public NoticeDivision(Long id, Notice notice, Division division) {
        this.id = id;
        this.notice = notice;
        this.division = division;
    }

}
