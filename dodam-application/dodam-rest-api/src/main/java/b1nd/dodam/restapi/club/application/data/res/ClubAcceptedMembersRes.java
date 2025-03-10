package b1nd.dodam.restapi.club.application.data.res;

import b1nd.dodam.restapi.member.application.data.res.StudentRes;

import java.util.List;
import java.util.ListIterator;

public record ClubAcceptedMembersRes(
        long clubId,
        String clubName,
        List<StudentRes> acceptedStudents
) {
}
