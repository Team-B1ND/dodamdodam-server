package b1nd.dodam.domain.rds.division.entity;

import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "division")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Division extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private String description;

    @Builder
    public Division(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void modify(String name) {
        this.name = name;
    }

}
