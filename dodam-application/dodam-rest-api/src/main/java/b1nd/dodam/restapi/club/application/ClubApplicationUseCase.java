package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.exception.ClubNotFoundException;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.domain.rds.club.service.ClubService;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.restapi.club.application.data.res.ClubAcceptedMembersRes;
import b1nd.dodam.restapi.club.application.data.res.ClubAllocationResultRes;
import b1nd.dodam.restapi.member.application.data.res.StudentRes;
import b1nd.dodam.restapi.support.assignment.ClubAllocator;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class ClubApplicationUseCase {
    private final ClubService clubService;
    private final ClubAllocator clubAllocator;
    private final ClubMemberService clubMemberService;
    private final StudentRepository studentRepository;

    public ResponseData<ClubAllocationResultRes> sortClubMembers() {
        List<Club> clubs = clubService.getCreativeActivityClubs();
        if (clubs.isEmpty()) {
            throw new ClubNotFoundException();
        }
        Map<Student, List<ClubMember>> studentApplications = getAllPendingMembers(clubs);
        ClubAllocationResultRes allocationResult = clubAllocator.allocate(clubs, studentApplications);

        List<ClubMember> toActivate = new ArrayList<>();
        for (ClubAcceptedMembersRes clubRes : allocationResult.clubAcceptedMembers()) {
            for (StudentRes studentRes : clubRes.acceptedStudents()) {
                toActivate.add(clubMemberService.getClubMemberByStudentAndClub(clubService.findById(clubRes.clubId()), studentRepository.getById(studentRes.id())));
            }
        }
        clubMemberService.updateStatus(toActivate, ClubStatus.ALLOWED);
        return ResponseData.ok("동아리 배정 성공", allocationResult);
    }

    private Map<Student, List<ClubMember>> getAllPendingMembers(List<Club> clubs) {
        return groupByStudent(
            clubs.stream()
                .flatMap(club -> clubMemberService.getPendingMembersByClub(club).stream())
                .toList()
        );
    }

    private Map<Student, List<ClubMember>> groupByStudent(List<ClubMember> members) {
        return members.stream()
            .collect(Collectors.groupingBy(ClubMember::getStudent));
    }
}