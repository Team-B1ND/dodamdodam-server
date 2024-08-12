package b1nd.dodam.domain.rds.point.entity;

import b1nd.dodam.domain.rds.point.enumeration.PointType;
import b1nd.dodam.domain.rds.point.enumeration.ScoreType;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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
        this.score = score;
        if(Objects.nonNull(reason)) {
            this.reason = reason;
        }
        if(Objects.nonNull(scoreType)) {
            this.scoreType = scoreType;
        }
        if(Objects.nonNull(pointType)) {
            this.pointType = pointType;
        }
    }

}
