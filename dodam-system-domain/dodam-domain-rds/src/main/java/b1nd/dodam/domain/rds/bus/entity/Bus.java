package b1nd.dodam.domain.rds.bus.entity;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.bus.enumeration.BusStatus;
import b1nd.dodam.domain.rds.bus.exception.BusFullOfSeatException;
import b1nd.dodam.domain.rds.bus.exception.BusPeriodExpiredException;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Consumer;

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
    @Enumerated(EnumType.STRING)
    private BusStatus status;

    @NotNull
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private int applyCount;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime leaveTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime timeRequired;

    @Builder
    public Bus(String busName, String description, int peopleLimit, BusStatus status, int applyCount, LocalDateTime leaveTime, LocalTime timeRequired) {
        this.busName = busName;
        this.description = description;
        this.peopleLimit = peopleLimit;
        this.status = status;
        this.applyCount = applyCount;
        this.leaveTime = leaveTime;
        this.timeRequired = timeRequired;
    }

    public void updateBus(String busName, String description, LocalDateTime leaveTime, LocalTime timeRequired, int peopleLimit) {
        checkIfTheBusHasDeparted();
        updateApplyIfNotEmpty(busName, value -> this.busName = value);
        updateApplyIfNotEmpty(description, value -> this.description = value);
        updateApplyIfNotEmpty(leaveTime, value -> this.leaveTime = value);
        updateApplyIfNotEmpty(timeRequired, value -> this.timeRequired = value);
        this.peopleLimit = peopleLimit;
    }

    private <T> void updateApplyIfNotEmpty(T newValue, Consumer<T> parameter) {
        if (newValue instanceof String ? StringUtils.isNotEmpty((String) newValue) : newValue != null) {
            parameter.accept(newValue);
        }
    }

    public void increaseApplyCount() {
        checkIfTheBusHasDeparted();
        checkIfThereAreSeatsAvailable();
        this.applyCount += 1;
    }

    public void decreaseApplyCount() {
        checkIfTheBusHasDeparted();
        this.applyCount -= 1;
    }

    private void checkIfTheBusHasDeparted() {
        if(leaveTime.isBefore(ZonedDateTimeUtil.nowToLocalDateTime())) {
            throw new BusPeriodExpiredException();
        }
    }

    private void checkIfThereAreSeatsAvailable() {
        if(applyCount == peopleLimit) {
            throw new BusFullOfSeatException();
        }
    }

    public void setStatus(BusStatus status) {
        this.status = status;
    }

}
