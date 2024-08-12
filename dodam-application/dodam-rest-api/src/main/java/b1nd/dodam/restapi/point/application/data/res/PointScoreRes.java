package b1nd.dodam.restapi.point.application.data.res;

import b1nd.dodam.domain.rds.point.entity.PointScore;
import b1nd.dodam.domain.rds.point.entity.Score;
import b1nd.dodam.domain.rds.point.enumeration.PointType;
import b1nd.dodam.restapi.member.application.data.res.StudentRes;

public record PointScoreRes(int id, int bonus, int minus, int offset, PointType type, StudentRes student) {
    public static PointScoreRes of(PointScore pointScore, PointType type) {
        Score score = type == PointType.DORMITORY ? pointScore.getDormitoryScore() : pointScore.getSchoolScore();

        return new PointScoreRes(
                pointScore.getId(),
                score.getBonus(),
                score.getMinus(),
                score.getOffset(),
                type,
                StudentRes.of(pointScore.getStudent())
        );
    }
}
