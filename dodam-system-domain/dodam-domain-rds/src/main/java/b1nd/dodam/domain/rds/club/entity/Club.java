package b1nd.dodam.domain.rds.club.entity;

import b1nd.dodam.domain.rds.club.enumeration.ClubState;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "club")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    private String subject;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ClubType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ClubState state;
}
