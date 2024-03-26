package b1nd.dodamcore.recruit.application.dto.req;

import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.recruit.application.dto.Pdf;
import b1nd.dodamcore.recruit.domain.entity.Recruit;
import b1nd.dodamcore.recruit.domain.entity.RecruitFile;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.stream.Collectors;

public record RecruitReq(@NotEmpty String name, @NotEmpty String location, @NotEmpty String duty,
                         String etc, @Positive Integer personnel, List<Pdf> pdfs) {

    public Recruit mapToRecruit(Member member) {
        Recruit recruit = Recruit.builder()
                .writer(member)
                .name(name)
                .location(location)
                .duty(duty)
                .etc(etc)
                .personnel(personnel)
                .build();

        pdfs.forEach(pdf -> recruit.addRecruitFile(
                RecruitFile.builder()
                        .pdfName(pdf.name())
                        .pdfUrl(pdf.url())
                        .build()
        ));

        return recruit;
    }

    public List<RecruitFile> updateRecruitFile(Recruit recruit) {
        return pdfs.stream().map(pdf ->
                        RecruitFile.builder()
                                .recruit(recruit)
                                .pdfName(pdf.name())
                                .pdfUrl(pdf.url())
                                .build()
                ).collect(Collectors.toList());
    }
}
