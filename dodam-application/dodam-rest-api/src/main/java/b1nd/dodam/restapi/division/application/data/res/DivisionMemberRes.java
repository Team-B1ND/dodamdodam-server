package b1nd.dodam.restapi.division.application.data.res;

import b1nd.dodam.domain.rds.division.entity.DivisionMember;
import b1nd.dodam.domain.rds.division.enumeration.DivisionPermission;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public record DivisionMemberRes(
        Long id,
        String memberId,
        String memberName,
        String profileImage,
        DivisionPermission permission,
        Integer grade,
        Integer room,
        Integer number,
        MemberRole role
) {

    public static DivisionMemberRes of(DivisionMember divisionMember, Optional<Student> student) {
        Member member = divisionMember.getMember();
        return new DivisionMemberRes(
                divisionMember.getId(),
                member.getId(),
                member.getName(),
                member.getProfileImage(),
                divisionMember.getPermission(),
                student.map(Student::getGrade).orElse(null),
                student.map(Student::getRoom).orElse(null),
                student.map(Student::getNumber).orElse(null),
                member.getRole()
        );
    }


    public static List<DivisionMemberRes> of(List<DivisionMember> divisionMembers, Map<Member, Student> studentMap) {
        return divisionMembers.stream()
                .map(dm -> DivisionMemberRes.of(dm, Optional.ofNullable(studentMap.get(dm.getMember()))))
                .toList();
    }
}
