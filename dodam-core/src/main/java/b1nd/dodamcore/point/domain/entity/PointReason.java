package b1nd.dodamcore.point.domain.entity;

import b1nd.dodamcore.common.entity.BaseEntity;
import b1nd.dodamcore.point.domain.enums.PointType;
import b1nd.dodamcore.point.domain.enums.ScoreType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static b1nd.dodamcore.common.util.ModifyUtil.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointReason extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private int score;

    @NotBlank
    @Column(unique = true)
    private String reason;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ScoreType scoreType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PointType pointType;

    @Builder
    public PointReason(int score, String reason, ScoreType scoreType, PointType pointType) {
        this.score = score;
        this.reason = reason;
        this.scoreType = scoreType;
        this.pointType = pointType;
    }

    public void update(int score, String reason, ScoreType scoreType, PointType pointType) {
        this.score = modifyIfNotZero(score, this.score);
        this.reason = modifyIfNotNull(reason, this.reason);
        this.scoreType = modifyIfNotNull(scoreType, this.scoreType);
        this.pointType = modifyIfNotNull(pointType, this.pointType);
    }

}
