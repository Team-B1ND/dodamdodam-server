package b1nd.dodam.restapi.bus.application.data.req;

import java.util.List;

public record BusApplicantReq(
        List<Integer> studentId,
        long busId
) {
}
