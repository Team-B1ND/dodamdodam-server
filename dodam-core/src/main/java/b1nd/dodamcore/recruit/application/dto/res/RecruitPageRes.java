package b1nd.dodamcore.recruit.application.dto.res;

import b1nd.dodamcore.recruit.domain.entity.Recruit;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public record RecruitPageRes(List<RecruitListRes> recruit, Integer nextPage) {

    public static RecruitPageRes of(Page<Recruit> recruitPage, Integer nextPage) {
        return new RecruitPageRes(recruitPage.getContent().stream().map(r ->
                new RecruitListRes(
                        r.getId(),
                        r.getWriter().getName(),
                        r.getName(),
                        r.getLocation(),
                        r.getDuty(),
                        r.getEtc(),
                        r.getPersonnel(),
                        r.getImage()
                )).collect(Collectors.toList()),
                nextPage);
    }

    public record RecruitListRes(int id, String writer, String name, String location, String duty,
                             String etc, int personnel, String image) {
    }
}
