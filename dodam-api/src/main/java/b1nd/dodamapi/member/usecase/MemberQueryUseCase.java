package b1nd.dodamapi.member.usecase;

import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.application.MemberSessionHolder;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.entity.Teacher;
import b1nd.dodamcore.member.domain.enums.AuthStatus;
import b1nd.dodamcore.member.domain.exception.MemberNotFoundException;
import b1nd.dodamcore.member.domain.vo.MemberInfoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryUseCase {

    private final MemberService service;
    private final MemberSessionHolder sessionHolder;

    public ResponseData<MemberInfoRes> getById(String id) {
        return ResponseData.ok("Id로 멤버 조회 성공", getMemberInfo(getMemberById(id)));
    }

    public ResponseData<MemberInfoRes> getMyInfo() {
        return ResponseData.ok("내 정보 조회 성공", getMemberInfo(sessionHolder.current()));
    }

    public ResponseData<List<MemberInfoRes>> searchByName(String name) {
        return ResponseData.ok("이름으로 검색 성공", service.searchByName(name).parallelStream()
                .map(this::getMemberInfo)
                .toList());
    }

    public ResponseData<List<MemberInfoRes>> getDeactivateMembers() {
        return ResponseData.ok("비활성화된 멤버 조회 성공", service.getByStatus(AuthStatus.DEACTIVATE).parallelStream()
                .map(this::getMemberInfo)
                .toList());
    }

    public ResponseData<List<MemberInfoRes>> getAll() {
        return ResponseData.ok("모든 멤버 정보 조회 성공", service.getByStatus(AuthStatus.ACTIVE).parallelStream()
                .map(this::getMemberInfo)
                .toList());
    }

    private MemberInfoRes getMemberInfo(Member member) {
        Student student = service.getStudentByMember(member)
                .orElse(null);
        Teacher teacher = service.getTeacherByMember(member)
                .orElse(null);
        return MemberInfoRes.of(member, student, teacher);
    }

    public ResponseData<Boolean> checkBroadcastClubMember() {
        return ResponseData.ok("방송부원 확인 성공", service.checkBroadcastClubMember(sessionHolder.current()));
    }

    public ResponseData<Boolean> checkBroadcastClubMember(String id) {
        Member member = getMemberById(id);
        return ResponseData.ok("방송부원 확인 성공", service.checkBroadcastClubMember(member));
    }

    private Member getMemberById(String id) {
        return service.getMemberById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

}