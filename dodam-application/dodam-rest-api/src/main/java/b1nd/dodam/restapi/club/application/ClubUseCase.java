package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.service.ClubService;
import b1nd.dodam.domain.rds.club.service.ClubStudentService;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.club.application.data.req.CreateClubReq;
import b1nd.dodam.restapi.club.application.data.req.UpdateClubInfoReq;
import b1nd.dodam.restapi.support.data.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ClubUseCase {
    private final ClubService clubService;
    private final ClubStudentService clubStudentService;
    private final StudentRepository studentRepository;
    private final MemberAuthenticationHolder authHolder;

    public Response save(CreateClubReq req) {
        clubService.checkIsNameDuplicated(req.name());
        Student leader = studentRepository.getByMember(authHolder.current());
        Club club = req.toEntity();
        clubService.save(club);
        clubStudentService.saveWithBuild(club, leader, studentRepository.getByIds(req.studentIds()));
        return Response.created("동아리 생성 완료");
    }

    public Response delete(Long id) {
        Club club = clubService.findById(id);
        clubStudentService.validateByClubLeader(club, authHolder.current());
        clubService.deleteClub(club);
        return Response.ok("동아리 삭제됨");
    }

    public Response update(Long id, UpdateClubInfoReq req) {
        Club club = clubService.findById(id);
        clubStudentService.validateByClubLeader(club, authHolder.current());
        club.updateInfo(req.name(), req.subject(), req.shortDescription(), req.description());
        clubService.save(club);
        return Response.ok("동아리 정보 업데이트됨");
    }
}
