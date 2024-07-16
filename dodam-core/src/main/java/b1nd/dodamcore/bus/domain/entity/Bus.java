package b1nd.dodamcore.bus.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Entity(name = "bus")
@DynamicUpdate
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

    public void updateBus(String busName, String description, LocalDateTime leaveTime, LocalTime timeRequired, int peopleLimit) {
        this.busName = busName;
        this.description = description;
        this.leaveTime = leaveTime;
        this.timeRequired = timeRequired;
        this.peopleLimit = peopleLimit;
    }

    @Builder
    public Bus(String busName, String description, int peopleLimit, LocalDateTime leaveTime, LocalTime timeRequired) {
        this.busName = busName;
        this.description = description;
        this.peopleLimit = peopleLimit;
        this.leaveTime = leaveTime;
        this.timeRequired = timeRequired;
    }

}
