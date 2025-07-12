package b1nd.dodam.restapi.nightstudy.application.data.res;

import java.util.List;

public record NightStudyAndProjectRes(
        List<NightStudyRes> selfStudy,
        List<StudentWithNightStudyProjectRes> projectStudy
) {
    public static NightStudyAndProjectRes of(
            List<NightStudyRes> selfStudy,
            List<StudentWithNightStudyProjectRes> projectStudy
    ) {
        return new NightStudyAndProjectRes(selfStudy, projectStudy);
    }
}
