package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubTime;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubTimeType;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.domain.rds.club.service.ClubService;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.member.repository.TeacherRepository;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.club.application.data.req.ClubTimeReq;
import b1nd.dodam.restapi.club.application.data.req.CreateClubReq;
import b1nd.dodam.restapi.club.application.data.req.UpdateClubInfoReq;
import b1nd.dodam.restapi.club.application.data.req.UpdateClubReq;
import b1nd.dodam.restapi.club.application.data.res.ClubDetailRes;
import b1nd.dodam.restapi.club.application.data.res.ClubTimeRes;
import b1nd.dodam.restapi.club.application.data.res.ClubWithLeaderRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ClubUseCase {
    private final ClubService clubService;
    private final ClubMemberService clubMemberService;
    private final StudentRepository studentRepository;
    private final MemberAuthenticationHolder authHolder;
    private final TeacherRepository teacherRepository;

    public Response save(ClubTimeReq req) {
        clubService.setClubTime(req.toEntity());
        return Response.created("시간 설정 성공");
    }

    public Response save(CreateClubReq req) {
        clubService.validateApplicationDuration(ClubTimeType.CLUB_CREATED);
        clubService.checkIsNameDuplicated(req.name());
        Club club = req.toEntity();
        Student leader = studentRepository.getByMember(authHolder.current());
        List<Student> students = studentRepository.getByIds(req.studentIds());
        clubMemberService.validateAndRejectLeader(club, leader, students);
        clubService.saveClubAndMember(club, leader, students);
        return Response.created("동아리 생성 성공");
    }

    public Response delete(Long id) {
        Club club = clubService.findById(id);
        clubMemberService.validateByClubLeader(club, authHolder.current());
        clubService.deleteClub(club);
        clubMemberService.setDeleteAllClubMembers(club);
        return Response.ok("동아리 삭제 성공");
    }

    public Response setWaiting(Long id) {
        Club club = clubService.findById(id);
        clubMemberService.validateActiveClubMemberSize(club, authHolder.current());
        clubMemberService.setDeleteClubMembers(club);
        club.updateStatus(ClubStatus.PENDING, null);
        clubService.update(club);
        return Response.ok("동아리 대기 성공");
    }

    public Response setTeacher(Long clubId) {
        Club club = clubService.findById(clubId);
        club.join(teacherRepository.getByMember(authHolder.current()));
        clubService.update(club);
        return Response.ok("당담 선생님 등록 성공");
    }

    public Response update(UpdateClubReq req) {
        List<Club> clubs = clubService.findByIds(req.clubIds());
        clubs.forEach(c -> c.updateStatus(req.status(), req.reason()));
        clubService.updateAll(clubs);
        return Response.ok("동아리 상태 변경 성공");
    }

    public ResponseData<ClubTimeRes> find() {
        ClubTime createTime = clubService.getClubTime(ClubTimeType.CLUB_CREATED);
        ClubTime applicantTime = clubService.getClubTime(ClubTimeType.CLUB_APPLICANT);
        return ResponseData.ok("시간 불러오기 성공", ClubTimeRes.of(createTime, applicantTime));
    }

    public Response updateInfo(Long id, UpdateClubInfoReq req) {
        Club club = clubService.findById(id);
        clubMemberService.validateByClubLeader(club, authHolder.current());
        club.updateInfo(req.name(), req.subject(), req.shortDescription(), req.description(), req.image());
        clubService.update(club);
        return Response.ok("동아리 정보 업데이트 성공");
    }

    @Transactional(readOnly = true)
    public ResponseData<List<ClubDetailRes>> getClubs() {
        return ResponseData.ok("전체 동아리 불러오기 성공", clubService.findAll().stream().map(ClubDetailRes::of).toList());
    }

    @Transactional(readOnly = true)
    public ResponseData<List<ClubWithLeaderRes>> getClubsWithLeader() {
        return ResponseData.ok("동아리, 리더 불러오기 성공", clubMemberService.getAllLeader().stream().map(ClubWithLeaderRes::of).toList());
    }

    @Transactional(readOnly = true)
    public ResponseData<ClubDetailRes> getClubDetail(Long id) {
        return ResponseData.ok("동아리 상세 정보 불러오기 성공", ClubDetailRes.of(clubService.findById(id)));
    }
}
