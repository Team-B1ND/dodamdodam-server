package b1nd.dodam.domain.rds.group.entity;

import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "`group`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @Builder
    public Group(String name) {
        this.name = name;
    }

    public void modify(String name) {
        this.name = name;
    }

}
