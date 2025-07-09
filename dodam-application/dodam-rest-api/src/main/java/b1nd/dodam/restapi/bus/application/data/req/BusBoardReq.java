package b1nd.dodam.restapi.bus.application.data.req;

import b1nd.dodam.domain.rds.bus.enumeration.BoardingType;

public record BusBoardReq(
        BoardingType boardingType
) {
}
