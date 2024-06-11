package b1nd.dodamcore.point.domain.vo;

import b1nd.dodamcore.point.domain.entity.PointScore;
import b1nd.dodamcore.point.domain.entity.Score;
import b1nd.dodamcore.point.domain.enums.PointType;

public record PointExcelRes(
        String name,
        Integer grade,
        Integer room,
        Integer number,
        Integer bonusPoint,
        Integer minusPoint,
        Integer offsetPoint
) {
    public static PointExcelRes of(PointScore ps, PointType pt) {
        Score score = (pt == PointType.SCHOOL) ?
                ps.getSchoolScore() : ps.getDormitoryScore();

        return new PointExcelRes(
                ps.getStudent().getMember().getName(),
                ps.getStudent().getGrade(),
                ps.getStudent().getRoom(),
                ps.getStudent().getNumber(),
                score.getBonus(),
                score.getMinus(),
                score.getOffset()
        );
    }
}