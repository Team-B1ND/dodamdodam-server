package b1nd.dodamapi.point.usecase.req;

import b1nd.dodamcore.point.domain.enums.PointType;
import b1nd.dodamcore.point.domain.enums.ScoreType;

public record ModifyPointReasonReq(PointType pointType, String reason, ScoreType scoreType, int score) {}
