package b1nd.dodam.restapi.bus.application.data.res;

import b1nd.dodam.domain.rds.bus.entity.BusApplicant;
import b1nd.dodam.domain.rds.bus.enumeration.BoardingType;
import b1nd.dodam.restapi.member.application.data.res.StudentRes;

public record StudentWithBusApplicantRes(
    BoardingType boardingType,
    int seat,
    StudentRes student
) {
    public static StudentWithBusApplicantRes of(BusApplicant busApplicant) {
        return new StudentWithBusApplicantRes(busApplicant.getBoardingType(), busApplicant.getSeat(), StudentRes.of(busApplicant.getStudent()));
    }
}
