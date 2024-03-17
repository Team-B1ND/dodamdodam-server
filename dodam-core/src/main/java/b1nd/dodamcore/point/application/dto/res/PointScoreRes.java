package b1nd.dodamcore.point.application.dto.res;

import b1nd.dodamcore.member.domain.vo.StudentRes;
import b1nd.dodamcore.point.domain.entity.PointScore;
import b1nd.dodamcore.point.domain.entity.Score;
import b1nd.dodamcore.point.domain.enums.PointType;

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