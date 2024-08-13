package b1nd.dodam.domain.rds.bus.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Entity(name = "bus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String busName;

    @NotNull
    private String description;

    @NotNull
    private int peopleLimit;

    @NotNull
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private int applyCount;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime leaveTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="HH:mm:ss")
    private LocalTime timeRequired;

    @Builder
    public Bus(String busName, String description, int peopleLimit, LocalDateTime leaveTime, LocalTime timeRequired) {
        this.busName = busName;
        this.description = description;
        this.peopleLimit = peopleLimit;
        this.leaveTime = leaveTime;
        this.timeRequired = timeRequired;
    }

    public void updateBus(String busName, String description, LocalDateTime leaveTime, LocalTime timeRequired, int peopleLimit) {
        if(Objects.nonNull(busName)) {
            this.busName = busName;
        }
        if(Objects.nonNull(description)) {
            this.description = description;
        }
        if(Objects.nonNull(leaveTime)) {
            this.leaveTime = leaveTime;
        }
        if(Objects.nonNull(timeRequired)) {
            this.timeRequired = timeRequired;
        }
        this.peopleLimit = peopleLimit;
    }

    public void increaseApplyCount() {
        this.applyCount += 1;
    }

    public void decreaseApplyCount() {
        this.applyCount = Math.max(0, applyCount - 1);
    }

    public boolean isFullOfSeat() {
        return peopleLimit == applyCount;
    }

    public boolean isExpired(LocalDateTime now) {
        return leaveTime.isBefore(now);
    }

}
