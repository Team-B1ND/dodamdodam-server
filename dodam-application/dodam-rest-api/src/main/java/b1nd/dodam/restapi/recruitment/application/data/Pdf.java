package b1nd.dodam.restapi.recruitment.application.data;

import b1nd.dodam.domain.rds.recruitment.entity.RecruitmentFile;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

public record Pdf(@NotEmpty String url, @NotEmpty String name) {
    public static List<Pdf> of(List<RecruitmentFile> recruitFiles) {
        return recruitFiles.stream().map(recruitFile ->
                new Pdf(recruitFile.getPdfUrl(), recruitFile.getPdfName())
        ).collect(Collectors.toList());
    }
}
