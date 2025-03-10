package b1nd.dodam.restapi.club.application.data.res;

import b1nd.dodam.restapi.member.application.data.res.StudentRes;

import java.util.List;

public record ClubAllocationResultRes(
        int assignedStudentCount,
        List<StudentRes> unassignedStudents,
        List<ClubAcceptedMembersRes> clubAcceptedMembers
) {
    public static ClubAllocationResultRes of(int size,  List<StudentRes> unassignedStudents, List<ClubAcceptedMembersRes> clubAcceptedMembers) {
        return new ClubAllocationResultRes(
            size,
            unassignedStudents,
            clubAcceptedMembers
        );
    }
}
