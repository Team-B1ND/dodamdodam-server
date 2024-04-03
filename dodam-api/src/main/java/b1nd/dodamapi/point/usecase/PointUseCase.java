package b1nd.dodamapi.point.usecase;

import b1nd.dodamapi.common.response.Response;
import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.entity.Teacher;
import b1nd.dodamcore.point.application.PointReasonService;
import b1nd.dodamcore.point.application.PointService;
import b1nd.dodamapi.point.usecase.req.IssuePointReq;
import b1nd.dodamcore.point.domain.vo.PointRes;
import b1nd.dodamcore.point.domain.vo.PointScoreRes;
import b1nd.dodamcore.point.domain.entity.Point;
import b1nd.dodamcore.point.domain.entity.PointReason;
import b1nd.dodamcore.point.domain.entity.PointScore;
import b1nd.dodamcore.point.domain.enums.PointType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointUseCase {

    private final PointService pointService;
    private final PointReasonService pointReasonService;
    private final MemberService memberService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(rollbackFor = Exception.class)
    public Response issue(IssuePointReq req) {
        PointReason reason = pointReasonService.getBy(req.reasonId());
        List<Student> students = memberService.getStudentsByIds(req.studentIds());

        savePoints(students, reason, req.issueAt());
        saveScores(students, reason);
        publishPointIssuedEvents(students, reason);
        return Response.created("상벌점 발급 성공");
    }

    private void savePoints(List<Student> students, PointReason reason, LocalDate issueAt) {
        Teacher teacher = memberService.getTeacherFromSession();
        pointService.save(students.parallelStream()
                .map(s -> Point.builder()
                        .student(s)
                        .teacher(teacher)
                        .reason(reason)
                        .issueAt(issueAt)
                        .build()
                ).toList()
        );
    }

    private void saveScores(List<Student> students, PointReason reason) {
        pointService.getScoresBy(students).parallelStream()
                .map(s -> s.issue(reason));
    }

    private void publishPointIssuedEvents(List<Student> students, PointReason reason) {
        students.forEach(s -> eventPublisher.publishEvent(
                PointSMSEventHelper.createIssuedEvent(s.getMember(), reason)
        ));
    }

    @Transactional(rollbackFor = Exception.class)
    public Response cancel(Integer id) {
        Point point = pointService.getPointBy(id);
        PointReason reason = point.getReason();
        Student student = point.getStudent();
        PointScore score = pointService.getScoreBy(student);
        score.cancel(reason);

        pointService.delete(point);
        publishCanceledEvent(reason, student.getMember());
        return Response.noContent("상벌점 취소 성공");
    }

    private void publishCanceledEvent(PointReason reason, Member member) {
        eventPublisher.publishEvent(PointSMSEventHelper.createCanceledEvent(member, reason));
    }

    public ResponseData<List<PointRes>> getMyPoints(PointType type) {
        Student student = memberService.getStudentFromSession();

        return ResponseData.ok("내 상벌점 조회 성공", getPointsBy(student, type));
    }

    public ResponseData<List<PointRes>> getPointsByStudent(Integer studentId, PointType type) {
        Student student = memberService.getStudentBy(studentId);
        return ResponseData.ok("학생별 상벌점 조회 성공", getPointsBy(student, type));
    }

    private List<PointRes> getPointsBy(Student student, PointType type) {
        return pointService.getPointsBy(student, type).parallelStream()
                .map(PointRes::of)
                .toList();
    }

    public ResponseData<PointScoreRes> getMyScore(PointType type) {
        Student student = memberService.getStudentFromSession();
        PointScore score = pointService.getScoreBy(student);
        PointScoreRes result = PointScoreRes.of(score, type);
        return ResponseData.ok("내 상벌점 점수 조회 성공", result);
    }

    public ResponseData<List<PointScoreRes>> getAllScores(PointType type) {
        List<PointScoreRes> result = pointService.getAllScores().parallelStream()
                .map(score -> PointScoreRes.of(score, type))
                .toList();
        return ResponseData.ok("모든 상벌점 점수 조회 성공", result);
    }

}
