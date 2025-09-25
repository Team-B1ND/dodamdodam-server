package b1nd.dodam.domain.rds.nightstudy.service;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProjectMember;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyProjectMemberNotFoundException;
import b1nd.dodam.domain.rds.nightstudy.repository.NightStudyProjectMemberRepository;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NightStudyProjectMemberService {
    private final NightStudyProjectMemberRepository repository;

    public void saveAll(List<NightStudyProjectMember> members) {
        repository.saveAll(members);
    }

    public List<NightStudyProjectMember> getAllStudentByDate(LocalDate now) {
        return repository.findAllowedProjectMembers(now);
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


    public List<NightStudyProjectMember> getAllowedProjectMembers(LocalDate date) {
        return repository.findMemberWithProjectByStatus(ApprovalStatus.ALLOWED, date);
    }

    public List<NightStudyProjectMember> getPendingProjectMembers(LocalDate date) {
        return repository.findMemberWithProjectByStatus(ApprovalStatus.PENDING, date);
    }
}