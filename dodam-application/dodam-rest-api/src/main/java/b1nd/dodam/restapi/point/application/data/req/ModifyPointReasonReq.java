package b1nd.dodam.restapi.point.application.data.req;

import b1nd.dodam.domain.rds.point.enumeration.PointType;
import b1nd.dodam.domain.rds.point.enumeration.ScoreType;

public record ModifyPointReasonReq(PointType pointType, String reason, ScoreType scoreType, int score) {}
