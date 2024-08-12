package b1nd.dodam.restapi.recruitment.application.data.res;

import b1nd.dodam.domain.rds.recruitment.entity.Recruitment;
import b1nd.dodam.restapi.recruitment.application.data.Pdf;

import java.util.List;

public record RecruitRes(String writer, String name, String location, String duty,
                         String etc, Integer personnel, String image, List<Pdf> pdfs) {
    public static RecruitRes of(Recruitment recruit) {
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
