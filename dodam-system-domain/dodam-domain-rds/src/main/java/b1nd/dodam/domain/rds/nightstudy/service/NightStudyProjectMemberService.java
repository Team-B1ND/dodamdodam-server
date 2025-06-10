package b1nd.dodam.domain.rds.nightstudy.service;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProjectMember;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectType;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyDuplicateException;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyProjectMemberNotFoundException;
import b1nd.dodam.domain.rds.nightstudy.repository.NightStudyProjectMemberRepository;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NightStudyProjectMemberService {
    private final NightStudyProjectMemberRepository repository;

    public void saveAll(List<NightStudyProjectMember> members) {
        repository.saveAll(members);
    }

    public List<NightStudyProjectMember> getAllStudentByDate(LocalDate now) {
        return repository.findAllowedProjectMembers(now, NightStudyProjectType.NIGHT_STUDY_PROJECT_2);
    }

    public List<NightStudyProject> findByStudent(Student student, LocalDate now) {
        return repository.findByStudentAndProject_EndAtGreaterThanEqual(student, now).stream().map(NightStudyProjectMember::getProject).toList();
    }

    public NightStudyProjectMember findByStudentAndProject(Student student, NightStudyProject project) {
        return repository.findByStudentAndProject(student, project)
                .orElseThrow(NightStudyProjectMemberNotFoundException::new);
    }

    public List<NightStudyProjectMember> getByProject(NightStudyProject project) {
        return repository.findAllByProject(project);
    }

    public void validateMultipleDurationDuplication(List<Student> students, LocalDate startAt, LocalDate endAt, NightStudyProjectType type) {
        if (repository.existsValidByStudentAndDate(students, startAt, endAt, type)) throw new NightStudyDuplicateException();
    }

    public List<NightStudyProjectMember> getPendingProjectMembers(LocalDate date) {
        return repository.findMemberWithProjectByStatus(ApprovalStatus.PENDING, date);
    }

    public List<NightStudyProjectMember> getAllowedProjectMembers(LocalDate date) {
        return repository.findMemberWithProjectByStatus(ApprovalStatus.ALLOWED, date);
    }

    public Map<NightStudyProject, List<Student>> groupMembersByProject(List<NightStudyProjectMember> members) {
        return members.stream()
                .collect(Collectors.groupingBy(
                        NightStudyProjectMember::getProject,
                        Collectors.mapping(NightStudyProjectMember::getStudent, Collectors.toList())
                ));
    }
}