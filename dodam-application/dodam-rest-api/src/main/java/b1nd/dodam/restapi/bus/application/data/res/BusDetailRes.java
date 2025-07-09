package b1nd.dodam.restapi.bus.application.data.res;

import java.util.List;

public record BusDetailRes(
        long id,
        String name,
        List<StudentWithBusApplicantRes> users
) {
    public static BusDetailRes of(long id, String name, List<StudentWithBusApplicantRes> users) {
        return new BusDetailRes(id, name, users);
    }
}
