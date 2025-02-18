package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.enumeration.ClubStudentStatus;
import b1nd.dodam.domain.rds.club.service.ClubService;
import b1nd.dodam.domain.rds.club.service.ClubStudentService;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.club.application.data.req.CreateClubReq;
import b1nd.dodam.restapi.support.data.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ClubUseCase {
    private final ClubService clubService;
    private final ClubStudentService clubStudentService;
    private final StudentRepository studentRepository;
    private final MemberAuthenticationHolder authHolder;

    public Response delete(UUID id) {
        Student student = studentRepository.getByMember(authHolder.current());
        Club club = clubService.findById(id);
        clubStudentService.validateClubMemberAndDirector(club, student);
        clubService.deleteClub(club);
        return Response.noContent("동아리 삭제됨");
    }

    public Response save(CreateClubReq req, List<Integer> studentIds) {
        Student director = studentRepository.getByMember(authHolder.current());
        clubService.checkIsNameDuplicated(req.name());
        Club club = req.toEntity();
        clubService.save(club);
        clubStudentService.saveDirector(club, director);
        clubStudentService.saveWithBuild(club, studentRepository.getByIds(studentIds), ClubStudentStatus.WAITING);
        return Response.created("동아리 생성 완료");
    }
}
