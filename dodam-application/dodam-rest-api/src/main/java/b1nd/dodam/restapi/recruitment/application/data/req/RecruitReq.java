package b1nd.dodam.restapi.recruitment.application.data.req;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.recruitment.entity.Recruitment;
import b1nd.dodam.domain.rds.recruitment.entity.RecruitmentFile;
import b1nd.dodam.restapi.recruitment.application.data.Pdf;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.stream.Collectors;

public record RecruitReq(@NotEmpty String name, @NotEmpty String location, @NotEmpty String duty,
                         String etc, @Positive Integer personnel, @NotEmpty String image, List<Pdf> pdfs) {

    public Recruitment mapToRecruit(Member member) {
        Recruitment recruit = Recruitment.builder()
                .writer(member)
                .name(name)
                .location(location)
                .duty(duty)
                .etc(etc)
                .personnel(personnel)
                .image(image)
                .build();

        pdfs.forEach(pdf -> recruit.addRecruitFile(
                RecruitmentFile.builder()
                        .pdfName(pdf.name())
                        .pdfUrl(pdf.url())
                        .build()
        ));

        return recruit;
    }

    public List<RecruitmentFile> updateRecruitFile(Recruitment recruitment) {
        return pdfs.stream().map(pdf ->
                        RecruitmentFile.builder()
                                .recruit(recruitment)
                                .pdfName(pdf.name())
                                .pdfUrl(pdf.url())
                                .build()
                ).collect(Collectors.toList());
    }

}
