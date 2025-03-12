package b1nd.dodam.restapi.bus.application.data.res;

import b1nd.dodam.domain.rds.bus.entity.BusTime;

import java.time.LocalDate;

public record BusTimeRes(
        int busTimeId,
        LocalDate startAt,
        LocalDate endAt
) {
    public static BusTimeRes of(BusTime busTime){
        return new BusTimeRes(
                busTime.getId(),
                busTime.getStartAt(),
                busTime.getEndAt()
        );
    }
}
