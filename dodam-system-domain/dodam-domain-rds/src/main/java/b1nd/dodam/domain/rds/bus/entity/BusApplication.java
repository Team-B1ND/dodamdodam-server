package b1nd.dodam.domain.rds.bus.entity;

import b1nd.dodam.domain.rds.member.entity.Student;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "bus_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "fk_student_id", nullable = false)
    private Student student;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_bus_id", nullable = false)
    private Bus bus;

    @Builder
    public BusApplication(Student student, Bus bus) {
        this.student = student;
        this.bus = bus;
    }

    public void updateBus(Bus bus) {
        this.bus = bus;
    }

}
