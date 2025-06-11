package b1nd.dodam.restapi.nightstudy.application.data.res;

import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProjectMember;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectMemberRole;

public record StudentWithProjectRoleRes(
        Integer id,
        String name,
        Integer grade,
        Integer room,
        Integer number,
        String profileImage,
        NightStudyProjectMemberRole role
) {
    public static StudentWithProjectRoleRes of(NightStudyProjectMember member) {
        return new StudentWithProjectRoleRes(
                member.getStudent().getId(),
                member.getStudent().getMember().getName(),
                member.getStudent().getGrade(),
                member.getStudent().getRoom(),
                member.getStudent().getNumber(),
                member.getStudent().getMember().getProfileImage(),
                member.getRole()
        );
    }
}