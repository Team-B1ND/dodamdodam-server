package b1nd.dodam.domain.rds.club.service;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubStudent;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStudentStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.club.exception.AlreadyInTheClubException;
import b1nd.dodam.domain.rds.club.repository.ClubMemberRepository;
import b1nd.dodam.domain.rds.member.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubStudentService {
    private final ClubMemberRepository clubMemberRepository;

    public void saveOwner(Club club, Student student) {
        rejectActivityClubMember(student);

        clubMemberRepository.save(ClubStudent.builder()
                .student(student)
                .clubStatus(ClubStudentStatus.ALLOWED)
                .club(club)
                .permission(ClubPermission.OWNER)
                .build()
        );
    }

    public void saveWithBuild(Club club, List<Student> students, ClubStudentStatus clubStudentStatus) {
        try {
            clubMemberRepository.saveAll(students.stream()
                    .map(student -> ClubStudent.builder()
                            .student(student)
                            .clubStatus(clubStudentStatus)
                            .club(club)
                            .permission(ClubPermission.MEMBER)
                            .build()
                    ).toList()
            );
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyInTheClubException();
        }
    }

    private void rejectActivityClubMember(Student student) {
        List<ClubStudent> clubStudents = clubMemberRepository.findAllByStudentAndClub_Type(student, ClubType.CREATIVE_ACTIVITY_CLUB);
        clubStudents.forEach(m -> m.modifyStatus(ClubStudentStatus.REJECTED));
        clubMemberRepository.saveAll(clubStudents);
    }
}
