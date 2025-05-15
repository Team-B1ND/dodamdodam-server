package b1nd.dodam.restapi.member.application.data.req;

import b1nd.dodam.domain.rds.member.entity.DormitoryManageMember;
import b1nd.dodam.domain.rds.member.entity.Member;
import jakarta.validation.constraints.NotBlank;

public record ApplyDormitoryManageMemberReq(@NotBlank String id) {
    public DormitoryManageMember toEntity(Member member) {
        return DormitoryManageMember.builder()
                .member(member)
                .build();
    }
}
