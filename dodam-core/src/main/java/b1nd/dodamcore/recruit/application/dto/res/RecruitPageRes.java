package b1nd.dodamcore.recruit.application.dto.res;

import b1nd.dodamcore.recruit.domain.entity.Recruit;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public record RecruitPageRes(List<RecruitListRes> recruitList, Integer nextPage) {

    public static RecruitPageRes of(Page<Recruit> recruitPage, Integer nextPage) {
        return new RecruitPageRes(recruitPage.getContent().stream().map(recruit ->
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

    public record RecruitListRes(int id, String writer, String name, String location, String duty, String etc, Integer personnel, String image) {
    }
}
