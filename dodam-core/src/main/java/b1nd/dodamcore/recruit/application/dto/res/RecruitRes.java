package b1nd.dodamcore.recruit.application.dto.res;

import b1nd.dodamcore.recruit.application.dto.Pdf;
import b1nd.dodamcore.recruit.domain.entity.Recruit;

import java.util.List;

public record RecruitRes(String writer, String name, String location, String duty,
                         String etc, Integer personnel, String image, List<Pdf> pdfs) {

    public static RecruitRes of(Recruit recruit) {
        return new RecruitRes(
                recruit.getWriter().getName(),
                recruit.getName(),
                recruit.getLocation(),
                recruit.getDuty(),
                recruit.getEtc(),
                recruit.getPersonnel(),
                recruit.getImage(),
                Pdf.of(recruit.getRecruitFiles())
        );
    }
}
