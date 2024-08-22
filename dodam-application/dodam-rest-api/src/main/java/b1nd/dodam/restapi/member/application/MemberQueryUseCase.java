package b1nd.dodam.restapi.member.application;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.service.MemberService;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.member.application.data.res.MemberInfoRes;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryUseCase {

    private final MemberService service;
    private final MemberAuthenticationHolder memberAuthenticationHolder;

    public ResponseData<MemberInfoRes> getById(String id) {
        return ResponseData.ok("Id로 멤버 조회 성공", getMemberInfo(service.getMemberBy(id)));
    }

    public ResponseData<MemberInfoRes> getMyInfo() {
        return ResponseData.ok("내 정보 조회 성공", getMemberInfo(memberAuthenticationHolder.current()));
    }

    public ResponseData<List<MemberInfoRes>> searchByName(String name) {
        return ResponseData.ok("이름으로 검색 성공", service.searchByName(name).parallelStream()
                .map(this::getMemberInfo)
                .toList());
    }

    public ResponseData<List<MemberInfoRes>> getDeactivateMembers() {
        return ResponseData.ok("비활성화된 멤버 조회 성공", service.getByStatus(ActiveStatus.DEACTIVATE).parallelStream()
                .map(this::getMemberInfo)
                .toList());
    }

    public ResponseData<List<MemberInfoRes>> getPendingMembers() {
        return ResponseData.ok("가입 대기 멤버 조회 성공", service.getByStatus(ActiveStatus.PENDING).parallelStream()
                .map(this::getMemberInfo)
                .toList());
    }

    public ResponseData<List<MemberInfoRes>> getAll() {
        return ResponseData.ok("모든 멤버 정보 조회 성공", service.getByStatus(ActiveStatus.ACTIVE).parallelStream()
                .map(this::getMemberInfo)
                .toList());
    }

    private MemberInfoRes getMemberInfo(Member member) {
        Student student = service.getStudentByMember(member)
                .orElse(null);
        Teacher teacher = service.getTeacherOrNullByMember(member)
                .orElse(null);
        return MemberInfoRes.of(member, student, teacher);
    }

    public ResponseData<Boolean> checkBroadcastClubMember() {
        return ResponseData.ok("방송부원 확인 성공", service.checkBroadcastClubMember(memberAuthenticationHolder.current()));
    }

    public ResponseData<Boolean> checkBroadcastClubMember(String id) {
        Member member = service.getMemberBy(id);
        return ResponseData.ok("방송부원 확인 성공", service.checkBroadcastClubMember(member));
    }

}
