package b1nd.dodamcore.point.application;

import b1nd.dodamcore.point.domain.entity.PointReason;
import b1nd.dodamcore.point.domain.enums.PointType;
import b1nd.dodamcore.point.domain.enums.ScoreType;

final class PointSMSHelper {

    private PointSMSHelper() {}

    static String getMessage(PointReason reason, String name, String action) {
        return "[대구SW고] " + name + " 학생 "
                + getScoreType(reason.getScoreType()) + " " + reason.getScore()+ "점 " +  action + "(["
                + getPointType(reason.getPointType()) + "] " + reason.getReason()+ ")";
    }

    static private String getScoreType(ScoreType type) {
        return type == ScoreType.MINUS ? "벌점" : "상점";
    }

    static private String getPointType(PointType type) {
        return type == PointType.SCHOOL ? "학교" : "기숙사";
    }

}