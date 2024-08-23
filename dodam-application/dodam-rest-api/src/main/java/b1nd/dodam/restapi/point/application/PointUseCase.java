package b1nd.dodam.restapi.point.application;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.member.repository.TeacherRepository;
import b1nd.dodam.domain.rds.point.entity.PointScore;
import b1nd.dodam.domain.rds.point.enumeration.PointType;
import b1nd.dodam.domain.rds.point.service.PointService;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.point.application.data.req.IssuePointReq;
import b1nd.dodam.restapi.point.application.data.res.PointRes;
import b1nd.dodam.restapi.point.application.data.res.PointScoreRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointUseCase {

    private final PointService pointService;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final MemberAuthenticationHolder memberAuthenticationHolder;

    @Transactional(rollbackFor = Exception.class)
    public Response issue(IssuePointReq req) {
        Teacher teacher = teacherRepository.getByMember(memberAuthenticationHolder.current());
        List<Student> students = studentRepository.getByIds(req.studentIds());
        pointService.issue(req.reasonId(), teacher, students, req.issueAt());
        return Response.created("상벌점 발급 성공");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response cancel(Integer id) {
        pointService.cancel(id);
        return Response.noContent("상벌점 취소 성공");
    }

    public ResponseData<List<PointRes>> getMyPoints(PointType type) {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        return ResponseData.ok("내 상벌점 조회 성공", getPointsBy(student, type));
    }

    public ResponseData<List<PointRes>> getPointsByStudent(int studentId, PointType type) {
        Student student = studentRepository.getById(studentId);
        return ResponseData.ok("학생별 상벌점 조회 성공", getPointsBy(student, type));
    }

    private List<PointRes> getPointsBy(Student student, PointType type) {
        return pointService.getPointsByStudentAndType(student, type)
                .parallelStream()
                .map(PointRes::of)
                .toList();
    }

    public ResponseData<PointScoreRes> getMyScore(PointType type) {
        Student student = studentRepository.getByMember(memberAuthenticationHolder.current());
        PointScore score = pointService.getScoreByStudent(student);
        return ResponseData.ok("내 상벌점 점수 조회 성공", PointScoreRes.of(score, type));
    }

    public ResponseData<List<PointScoreRes>> getAllScores(PointType type) {
        return ResponseData.ok("모든 상벌점 점수 조회 성공", pointService.getAllScores()
                .parallelStream()
                .filter(score -> score.getStudent().getMember().getStatus() == ActiveStatus.ACTIVE)
                .map(score -> PointScoreRes.of(score, type))
                .toList());
    }

}
