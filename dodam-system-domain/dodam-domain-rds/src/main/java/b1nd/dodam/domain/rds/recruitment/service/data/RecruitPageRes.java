package b1nd.dodam.domain.rds.recruitment.service.data;

import b1nd.dodam.domain.rds.recruitment.entity.Recruitment;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public record RecruitPageRes(List<RecruitListRes> recruitList, Integer nextPage) {
    public static RecruitPageRes of(Page<Recruitment> recruitmentList, Integer nextPage) {
        return new RecruitPageRes(recruitmentList.getContent().stream().map(recruit ->
                new RecruitListRes(
                        recruit.getId(),
                        recruit.getWriter().getName(),
                        recruit.getName(),
                        recruit.getLocation(),
                        recruit.getDuty(),
                        recruit.getEtc(),
                        recruit.getPersonnel(),
                        recruit.getImage()
                )).collect(Collectors.toList()),
                nextPage);
    }

    public record RecruitListRes(int id, String writer, String name, String location, String duty, String etc, Integer personnel, String image) {}

}
