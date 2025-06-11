package b1nd.dodam.restapi.member.application;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.domain.rds.member.repository.*;
import b1nd.dodam.domain.rds.member.service.MemberService;
import b1nd.dodam.domain.redis.member.model.MemberInfoRedisModel;
import b1nd.dodam.domain.redis.member.service.MemberSearchRedisService;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.member.application.data.res.MemberInfoRes;
import b1nd.dodam.restapi.member.application.data.res.StudentRelationRes;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryUseCase {

    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final BroadcastClubMemberRepository broadcastClubMemberRepository;
    private final DormitoryManageMemberRepository dormitoryManageMemberRepository;
    private final MemberService memberService;
    private final MemberSearchRedisService memberSearchRedisService;
    private final MemberAuthenticationHolder memberAuthenticationHolder;

    public ResponseData<MemberInfoRes> getById(String id) {
        return ResponseData.ok("Id로 멤버 조회 성공", getMemberInfo(memberRepository.getById(id)));
    }

    public ResponseData<MemberInfoRes> getMyInfo() {
        return ResponseData.ok("내 정보 조회 성공", getMemberInfo(memberAuthenticationHolder.current()));
    }

    public ResponseData<List<MemberInfoRes>> searchByMemberInfo(String name, Integer grade, MemberRole role, ActiveStatus status, long page, long pageSize) {
        List<MemberInfoRedisModel> memberInfoList = memberSearchRedisService.searchMembers(
                name, grade, Objects.toString(role, null), Objects.toString(status,null), page, pageSize
        );
        return ResponseData.ok("멤버 정보로 검색 성공", MemberInfoRes.fromRedisModel(memberInfoList));
    }

    public ResponseData<List<MemberInfoRes>> searchByMember(String name, Integer grade, MemberRole role, ActiveStatus status, long page, long pageSize) {
        List<MemberInfoRes> res = memberRepository.searchMembers(name, grade, role, status, PageRequest.of((int) page, (int) pageSize))
                .stream().map(this::getMemberInfo).toList();
        return ResponseData.ok("멤버 정보로 검색 성공", res);
    }

    public ResponseData<List<MemberInfoRes>> getMembersByStatus(ActiveStatus status) {
        return ResponseData.ok("상태별 멤버 조회 성공", memberRepository.findByStatusOrderByStudent(status)
                .parallelStream()
                .map(this::getMemberInfo)
                .toList());
    }

    private MemberInfoRes getMemberInfo(Member member) {
        Student student = studentRepository.findByMember(member)
                .orElse(null);
        Teacher teacher = teacherRepository.findByMember(member)
                .orElse(null);
        return MemberInfoRes.of(member, student, teacher);
    }

    public ResponseData<List<MemberInfoRes>> getAll(){
        return ResponseData.ok("모든 멤버 정보 조회 성공",
                memberRepository.findByStatusOrderByStudent(ActiveStatus.ACTIVE)
                        .parallelStream()
                        .map(this::getMemberInfo)
                        .toList());
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

    public ResponseData<Boolean> checkDormitoryManageMember() {
        Member member = memberAuthenticationHolder.current();
        return checkDormitoryManageMemberByMember(member);
    }

    public ResponseData<Boolean> checkDormitoryManageMember(String id) {
        Member member = memberRepository.getById(id);
        return checkDormitoryManageMemberByMember(member);
    }

    private ResponseData<Boolean> checkDormitoryManageMemberByMember(Member member) {
        return ResponseData.ok("자치위원 확인 성공", dormitoryManageMemberRepository.existsByMember(member));
    }

    public ResponseData<MemberInfoRes> getMemberByCode(String code){
        return ResponseData.ok("학생 조회 성공", this.getMemberInfo(memberService.checkCode(code)
                .getMember()));
    }

    public ResponseData<List<StudentRelationRes>> getStudentByPatent(){
        Member member = memberAuthenticationHolder.current();
        List<StudentRelationRes> studentRelationRes =
                memberService.getStudentRelationByMember(member).stream()
                        .map(StudentRelationRes::of).toList();
        return ResponseData.ok("자녀 조회 성공", studentRelationRes);
    }

}