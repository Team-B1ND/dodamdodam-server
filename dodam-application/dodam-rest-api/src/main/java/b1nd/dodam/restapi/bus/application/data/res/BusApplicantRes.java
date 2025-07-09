package b1nd.dodam.restapi.bus.application.data.res;

import b1nd.dodam.domain.rds.bus.entity.BusApplicant;
import b1nd.dodam.domain.rds.bus.enumeration.BoardingType;

public record BusApplicantRes(
        long id,
        String name,
        int seat,
        BoardingType boardingType
) {
    public static BusApplicantRes of(BusApplicant busApplicant) {
        if (busApplicant == null)
            return null;
        return new BusApplicantRes(busApplicant.getBus().getId(), busApplicant.getBus().getName(), busApplicant.getSeat(), busApplicant.getBoardingType());
    }
}