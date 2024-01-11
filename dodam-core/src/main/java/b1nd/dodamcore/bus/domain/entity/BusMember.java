package b1nd.dodamcore.bus.domain.entity;

import b1nd.dodamcore.member.domain.entity.Student;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@DynamicUpdate
@Entity(name = "bus_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "fk_student_id")
    private Student student;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_bus_id", nullable = false)
    private Bus bus;

    public void modifyBus(Bus bus) {
        this.bus = bus;
    }

    @Builder
    public BusMember(Student student, Bus bus) {
        this.student = student;
        this.bus = bus;
    }
}
