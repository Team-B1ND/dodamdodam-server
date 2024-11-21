package b1nd.dodam.restapi.division.application.data.res;

import b1nd.dodam.domain.rds.division.entity.DivisionMember;
import b1nd.dodam.domain.rds.division.enumeration.DivisionPermission;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;

import java.util.List;

public record DivisionMemberRes(
        Long id,
        String memberName,
        String profileImage,
        DivisionPermission permission,
        int grade,
        int room,
        int number
) {

    static public DivisionMemberRes of(DivisionMember divisionMember, Student student){
        Member member = divisionMember.getMember();
        return new DivisionMemberRes(
                divisionMember.getId(),
                member.getId(),
                member.getName(),
                member.getProfileImage(),
                divisionMember.getPermission(),

        );
    }
}
