package b1nd.dodam.restapi.member.application;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.repository.BroadcastClubMemberRepository;
import b1nd.dodam.domain.rds.member.repository.MemberRepository;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.member.repository.TeacherRepository;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.member.application.data.res.MemberInfoRes;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryUseCase {

    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final BroadcastClubMemberRepository broadcastClubMemberRepository;
    private final MemberAuthenticationHolder memberAuthenticationHolder;

    public ResponseData<MemberInfoRes> getById(String id) {
        return ResponseData.ok("Id로 멤버 조회 성공", getMemberInfo(memberRepository.getById(id)));
    }

    public ResponseData<MemberInfoRes> getMyInfo() {
        return ResponseData.ok("내 정보 조회 성공", getMemberInfo(memberAuthenticationHolder.current()));
    }

    public ResponseData<List<MemberInfoRes>> searchByName(String name) {
        return ResponseData.ok("이름으로 검색 성공", memberRepository.findByNameContains(name)
                .parallelStream()
                .map(this::getMemberInfo)
                .toList());
    }

    public ResponseData<List<MemberInfoRes>> getDeactivateMembers() {
        return ResponseData.ok("비활성화된 멤버 조회 성공", memberRepository.findByStatusOrderByStudent(ActiveStatus.DEACTIVATE)
                .parallelStream()
                .map(this::getMemberInfo)
                .toList());
    }

    @Cacheable(value = "members-cache", key = "'activeMembers'")
    public List<MemberInfoRes> getAll() {
        return memberRepository.findByStatusOrderByStudent(ActiveStatus.ACTIVE).parallelStream()
                .map(this::getMemberInfo)
                .toList();
    }

    private MemberInfoRes getMemberInfo(Member member) {
        Student student = studentRepository.findByMember(member)
                .orElse(null);
        Teacher teacher = teacherRepository.findByMember(member)
                .orElse(null);
        return MemberInfoRes.of(member, student, teacher);
    }

    public ResponseData<Boolean> checkBroadcastClubMember() {
        Member member = memberAuthenticationHolder.current();
        return checkBroadcastClubMemberByMember(member);
    }

    public ResponseData<Boolean> checkBroadcastClubMember(String id) {
        Member member = memberRepository.getById(id);
        return checkBroadcastClubMemberByMember(member);
    }

    private ResponseData<Boolean> checkBroadcastClubMemberByMember(Member member) {
        return ResponseData.ok("방송부원 확인 성공", broadcastClubMemberRepository.existsByMember(member));
    }

}
