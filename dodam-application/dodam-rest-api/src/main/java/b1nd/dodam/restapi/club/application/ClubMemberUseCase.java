package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubTimeType;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.domain.rds.club.service.ClubService;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.club.application.data.req.ClubPassReq;
import b1nd.dodam.restapi.club.application.data.req.JoinClubMemberReq;
import b1nd.dodam.restapi.club.application.data.res.*;
import b1nd.dodam.restapi.member.application.data.res.StudentWithImageRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ClubMemberUseCase {
    private final ClubService clubService;
    private final ClubMemberService clubMemberService;
    private final MemberAuthenticationHolder authenticationHolder;
    private final StudentRepository studentRepository;

    public Response joinClub(JoinClubMemberReq req) {
        clubService.validateApplicationDuration(ClubTimeType.CLUB_APPLICANT);
        Student student = studentRepository.getByMember(authenticationHolder.current());
        clubMemberService.validateFirstGrade(student);
        clubMemberService.validateNoActiveCreativeClub(student);
        Club club = clubMemberService.findClubIfNotClubMember(req.clubId(), ClubStatus.ALLOWED, student, ClubStatus.DELETED);
        clubMemberService.saveAndValidateClubMembers(List.of(req.toEntity(student, club)));
        return Response.ok("동아리 입부 신청 성공");
    }

    public Response setClubMemberStatus(ClubPassReq req) {
        clubMemberService.validateByClubLeader(clubService.findById(req.clubId()), authenticationHolder.current());
        clubMemberService.setStatusStudentClub(req.studentId(), req.clubId(), req.status());
        return Response.ok("동아리 가입 상태 변경 성공");
    }

    public Response updateClubJoinRequestReceived(Long id, ClubStatus clubStatus) {
        clubMemberService.setClubMemberStatus(id,authenticationHolder.current(), clubStatus);
        return Response.ok("동아리 가입 업데이트 성공");
    }

    @Transactional(readOnly = true)
    public ResponseData<List<StudentWithImageRes>> getGradeStudents(boolean isSelf) {
        Member member = authenticationHolder.current();
        return ResponseData.ok("학년 불러오기 성공", isSelf
            ? clubMemberService.getAllGradeStudent(member).stream().map(StudentWithImageRes::of).toList()
            : clubMemberService.getSecondGradeStudent(member).stream().map(StudentWithImageRes::of).toList()
        );
    }

    @Transactional(readOnly = true)
    public ResponseData<List<ClubMemberRes>> getClubJoinRequestsReceived() {
        return ResponseData.ok("받은 부원 제안 불러오기 성공", clubMemberService.getJoinRequests(authenticationHolder.current(), ClubStatus.WAITING).stream().map(ClubMemberRes::of).toList());
    }

    @Transactional(readOnly = true)
    public ResponseData<ClubStudentRes> getClubLeader(Long id) {
        return ResponseData.ok("부장 불러오기 성공", ClubStudentRes.of(clubMemberService.getClubLeader(id)));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<ClubJoinStudentRes>> getPendingClubMembers(Long id) {
        return ResponseData.ok("입부 희망 인원 불러오기 성공", clubMemberService.getStatusClubMembers(id, ClubStatus.PENDING).stream().map(ClubJoinStudentRes::of).toList());
    }

    @Transactional(readOnly = true)
    public ResponseData<ClubStudentListRes> getAllClubMembers(Long id) {
        return ResponseData.ok("동아리 멤버 불러오기 성공", getClubMembersByRole(id, clubMemberService.isClubLeader(id, authenticationHolder.current())));
    }

    public ResponseData<List<ClubMemberRes>> getMemberJoinRequests(int studentId) {
        Student student = studentRepository.getById(studentId);
        return ResponseData.ok("동아리 지망 불러오기 성공", clubMemberService.findAllCreativeClubByStudent(student).stream().map(ClubMemberRes::of).toList());
    }

    @Transactional(readOnly = true)
    public ResponseData<List<ClubMemberRes>> getStudentJoinRequest() {
        return ResponseData.ok("나의 동아리 입부 신청 불러오기 성공", clubMemberService.getJoinRequests(authenticationHolder.current(), ClubStatus.PENDING).stream().map(ClubMemberRes::of).toList());
    }

    @Transactional(readOnly = true)
    public ResponseData<List<ClubStatusRes>> getJoinedClubs() {
        return ResponseData.ok("나의 동아리 상태 불러오기 성공", clubMemberService.findUserAllowedClub(authenticationHolder.current()).stream().map(ClubStatusRes::of).toList());
    }

    @Transactional(readOnly = true)
    public ResponseData<List<ClubDetailRes>> getStudentClubStatus() {
        return ResponseData.ok("동아리 불러오기 성공", clubMemberService.getStudentClubStatus(studentRepository.getByMember(authenticationHolder.current())).stream().map(ClubDetailRes::of).toList());
    }

    private ClubStudentListRes getClubMembersByRole(Long id, boolean isLeader) {
        return ClubStudentListRes.of(
                isLeader,
                isLeader
                    ? clubMemberService.getAllClubMembers(id).stream().map(ClubStudentRes::of).toList()
                    : clubMemberService.getStatusClubMembers(id, ClubStatus.ALLOWED).stream().map(ClubStudentRes::of).toList()
        );
    }

}
