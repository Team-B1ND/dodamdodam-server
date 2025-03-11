package b1nd.dodam.domain.rds.bus.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Entity(name = "bus_preset")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusPreset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    private String description;

    private int peopleLimit;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime leaveTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime timeRequired;

    @Builder
    public BusPreset(String name, String description, int peopleLimit, LocalTime leaveTime, LocalTime timeRequired) {
        this.name = name;
        this.description = description;
        this.peopleLimit = peopleLimit;
        this.leaveTime = leaveTime;
        this.timeRequired = timeRequired;
    }

}
