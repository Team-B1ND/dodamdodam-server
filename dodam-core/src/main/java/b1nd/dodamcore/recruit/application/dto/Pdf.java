package b1nd.dodamcore.recruit.application.dto;

import b1nd.dodamcore.recruit.domain.entity.RecruitFile;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

public record Pdf(@NotEmpty String url, @NotEmpty String name) {

    public static List<Pdf> of(List<RecruitFile> recruitFiles) {
        return recruitFiles.stream().map(recruitFile ->
                new Pdf(recruitFile.getPdfUrl(), recruitFile.getPdfName())
        ).collect(Collectors.toList());
    }
}
