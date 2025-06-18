package b1nd.dodam.restapi.nightstudy.application.data.res;

import java.util.List;

public record NightStudyAndProjectRes(
        List<NightStudyRes> nightStudyRes,
        List<StudentWithNightStudyProjectRes> studentWithNightStudyProjectRes
) {
    public static NightStudyAndProjectRes of(
            List<NightStudyRes> nightStudyRes,
            List<StudentWithNightStudyProjectRes> studentWithNightStudyProjectRes
    ) {
        return new NightStudyAndProjectRes(nightStudyRes, studentWithNightStudyProjectRes);
    }
}
