package b1nd.dodam.domain.rds.bus.entity;

import b1nd.dodam.domain.rds.bus.enumeration.BoardingType;
import b1nd.dodam.domain.rds.member.entity.Student;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusApplicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_bus_id")
    private Bus bus;

    private int seat;

    @Enumerated(EnumType.STRING)
    private BoardingType boardingType;

    @Builder
    public BusApplicant(Student student, Bus bus, BoardingType boardingType) {
        this.student = student;
        this.bus = bus;
        this.boardingType = boardingType;
    }

    public void updateBoardingType(BoardingType boardingType) {
        this.boardingType = boardingType;
    }

    public void updateSeat(int seat) {
        this.seat = seat;
    }
}