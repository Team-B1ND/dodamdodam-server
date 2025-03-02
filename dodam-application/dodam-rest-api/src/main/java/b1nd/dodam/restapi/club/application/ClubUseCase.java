package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubTimeType;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.domain.rds.club.service.ClubService;
import b1nd.dodam.domain.rds.club.service.ClubTimeService;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.club.application.data.req.CreateClubReq;
import b1nd.dodam.restapi.club.application.data.req.UpdateClubInfoReq;
import b1nd.dodam.restapi.club.application.data.req.UpdateClubReq;
import b1nd.dodam.restapi.club.application.data.res.ClubDetailRes;
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
    private final ClubTimeService clubTimeService;
    private final ClubMemberService clubMemberService;
    private final StudentRepository studentRepository;
    private final MemberAuthenticationHolder authHolder;

    public Response save(CreateClubReq req) {
        clubTimeService.validateApplicationDuration(ClubTimeType.CLUB_CREATE);
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
        return Response.ok("동아리 삭제 성공");
    }

    public Response setWaiting(Long id) {
        Club club = clubService.findById(id);
        clubMemberService.validateActiveClubMemberSize(club, authHolder.current());
        club.updateStatus(ClubStatus.PENDING, null);
        clubService.update(club);
        return Response.ok("동아리 대기 성공");
    }


    public Response update(Long id, UpdateClubReq req) {
        Club club = clubService.findById(id);
        club.updateStatus(req.status(), req.reason());
        clubService.update(club);
        return Response.ok("동아리 상태 변경 성공");
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
    public ResponseData<ClubDetailRes> getClubDetail(Long id) {
        return ResponseData.ok("동아리 상세 정보 불러오기 성공", ClubDetailRes.of(clubService.findById(id)));
    }
}
