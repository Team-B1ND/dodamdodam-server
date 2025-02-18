package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.enumeration.ClubMemberStatus;
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

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ClubUseCase {
    private final ClubService clubService;
    private final ClubStudentService clubStudentService;
    private final StudentRepository studentRepository;
    private final MemberAuthenticationHolder authHolder;

    public Response save(CreateClubReq req, List<Integer> studentIds) {
        clubService.checkIsNameDuplicated(req.name());
        Club club = req.toEntity();
        clubService.save(club);
        Student owner = studentRepository.getByMember(authHolder.current());
        clubStudentService.saveOwner(club, owner);
        clubStudentService.saveWithBuild(club, studentRepository.getByIds(studentIds), ClubMemberStatus.WAITING);
        return Response.created("동아리 생성 완료");
    }
}
