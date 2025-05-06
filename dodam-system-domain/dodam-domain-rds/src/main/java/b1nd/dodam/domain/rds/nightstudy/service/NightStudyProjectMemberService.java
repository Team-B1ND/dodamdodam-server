package b1nd.dodam.domain.rds.nightstudy.service;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudy;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProjectMember;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyProjectMemberNotFoundException;
import b1nd.dodam.domain.rds.nightstudy.repository.NightStudyProjectMemberRepository;
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

    public NightStudyProjectMember findByStudentAndProject(Student student, NightStudyProject project) {
        return repository.findByStudentAndProject(student, project)
                .orElseThrow(NightStudyProjectMemberNotFoundException::new);
    }

    public List<NightStudyProjectMember> getAllByProject(NightStudyProject project) {
        return repository.findAllByProject(project);
    }

    public boolean checkMultipleDurationDuplication(Student leader, List<Student> students, LocalDate startAt, LocalDate endAt) {
        students.add(leader);
        return students.stream().anyMatch(student -> repository.existsValidByStudentAndDate(student, startAt, endAt));
    }
}
