package b1nd.dodamapi.point.usecase;

import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.point.domain.entity.PointReason;
import b1nd.dodamcore.point.domain.enums.PointType;
import b1nd.dodamcore.point.domain.enums.ScoreType;
import b1nd.dodamcore.point.domain.event.PointCanceledEvent;
import b1nd.dodamcore.point.domain.event.PointIssuedEvent;

final class PointSMSEventHelper {

    public static PointIssuedEvent createIssuedEvent(Member member, PointReason reason) {
        return new PointIssuedEvent(
                createMessage(reason, member.getName(), "발급"), member.getPhone()
        );
    }

    public static PointCanceledEvent createCanceledEvent(Member member, PointReason reason) {
        return new PointCanceledEvent(
                createMessage(reason, member.getName(), "취소"), member.getPhone()
        );
    }

    private static String createMessage(PointReason reason, String name, String action) {
        return "[대구SW고] " + name + " 학생 "
                + getScoreType(reason.getScoreType()) + " " + reason.getScore()+ "점 " +  action + "(["
                + getPointType(reason.getPointType()) + "] " + reason.getReason()+ ")";
    }

    private static String getScoreType(ScoreType type) {
        return type == ScoreType.MINUS ? "벌점" : "상점";
    }

    private static String getPointType(PointType type) {
        return type == PointType.SCHOOL ? "학교" : "기숙사";
    }

}
