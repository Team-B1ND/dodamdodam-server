package b1nd.dodam.domain.rds.bus.entity;

import b1nd.dodam.domain.rds.bus.enumeration.BusApplicationStatus;
import b1nd.dodam.domain.rds.member.entity.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Enumerated(EnumType.STRING)
    private BusApplicationStatus status;

    @NotNull
    private Integer seatNumber;

    @OneToOne
    @JoinColumn(name = "fk_student_id", nullable = false)
    private Student student;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_bus_id", nullable = false)
    private Bus bus;

    @Builder
    public BusApplication(int id, BusApplicationStatus status, int seatNumber, Student student, Bus bus) {
        this.id = id;
        this.status = status;
        this.seatNumber = seatNumber;
        this.student = student;
        this.bus = bus;
    }

    public void updateBus(Bus bus, Integer seatNumber) {
        this.bus = bus;
        if (seatNumber != null) {
            this.seatNumber = seatNumber;
        }
    }

    public void updateStatus(BusApplicationStatus status) {
        this.status = status;
    }

}
