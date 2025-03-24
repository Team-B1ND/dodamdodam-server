package b1nd.dodam.domain.rds.bus.entity;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.bus.exception.BusTimeUnableException;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "bus_time")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startAt;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endAt;

    @Builder
    public BusTime(LocalDate startAt, LocalDate endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public void checkIfTheBusIsAvailable() {
        LocalDate today = ZonedDateTimeUtil.nowToLocalDate();

        if (today.isBefore(startAt) || today.isAfter(endAt)) {
            throw new BusTimeUnableException();
        }
    }

}
