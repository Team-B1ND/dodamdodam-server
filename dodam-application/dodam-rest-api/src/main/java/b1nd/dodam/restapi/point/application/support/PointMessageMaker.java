package b1nd.dodam.restapi.point.application.support;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.point.entity.PointReason;
import b1nd.dodam.domain.rds.point.enumeration.PointType;
import b1nd.dodam.domain.rds.point.enumeration.ScoreType;
import b1nd.dodam.domain.rds.point.event.PointCanceledEvent;
import b1nd.dodam.domain.rds.point.event.PointIssuedEvent;

public final class PointMessageMaker {

    private PointMessageMaker() {}

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
