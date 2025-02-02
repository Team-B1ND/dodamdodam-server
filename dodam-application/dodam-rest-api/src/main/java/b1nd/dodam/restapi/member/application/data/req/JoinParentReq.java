package b1nd.dodam.restapi.member.application.data.req;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Parent;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record JoinParentReq(@NotEmpty String id, @NotEmpty String pw, @NotEmpty String name, @NotEmpty @Email String email,
                            List<ConnectStudentReq> relationInfo, @NotEmpty String phone) {
    public Parent mapToParent(Member member) {
        return Parent.builder()
                .member(member)
                .build();
    }

    public Member mapToMember(String encodedPw) {
        return Member.builder()
                .id(id)
                .pw(encodedPw)
                .email(email)
                .name(name)
                .role(MemberRole.PARENT)
                .phone(phone)
                .status(ActiveStatus.PENDING)
                .build();
    }

}
