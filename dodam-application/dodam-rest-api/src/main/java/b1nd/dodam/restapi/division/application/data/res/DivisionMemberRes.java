package b1nd.dodam.restapi.division.application.data.res;

import b1nd.dodam.domain.rds.division.entity.DivisionMember;
import b1nd.dodam.domain.rds.division.enumeration.DivisionPermission;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;

import java.util.List;
import java.util.Optional;

public record DivisionMemberRes(
        Long id,
        String memberName,
        String profileImage,
        DivisionPermission permission,
        Integer grade,
        Integer room,
        Integer number
) {

    static public DivisionMemberRes of(DivisionMember divisionMember, Student student){
        Member member = divisionMember.getMember();
        return new DivisionMemberRes(
                divisionMember.getId(),
                member.getName(),
                member.getProfileImage(),
                divisionMember.getPermission(),
                Optional.ofNullable(student).map(Student::getGrade).orElse(null),
                Optional.ofNullable(student).map(Student::getRoom).orElse(null),
                Optional.ofNullable(student).map(Student::getNumber).orElse(null)
        );
    }
}
