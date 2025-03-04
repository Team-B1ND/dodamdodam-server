package b1nd.dodam.domain.rds.club.entity;

import b1nd.dodam.domain.rds.club.enumeration.ClubTimeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity(name = "club_of_time")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubTime {
    @Id
    @Enumerated(EnumType.STRING)
    private ClubTimeType id;

    @NotNull
    private LocalDate start;

    @NotNull
    private LocalDate end;

    @Builder
    public ClubTime(ClubTimeType id, LocalDate start, LocalDate end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }
}