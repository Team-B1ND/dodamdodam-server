package b1nd.dodam.restapi.group.application.data.res;

import b1nd.dodam.domain.rds.group.entity.GroupMember;
import b1nd.dodam.domain.rds.group.enumeration.GroupPermission;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;

import java.util.List;

public record GroupMemberRes(
        Long id,
        String memberId,
        String memberName,
        MemberRole role,
        String profileImage,
        GroupPermission permission
) {
    static public List<GroupMemberRes> of(List<GroupMember> groupMembers){
        return groupMembers.stream().map(GroupMemberRes::of).toList();
    }

    static private GroupMemberRes of(GroupMember groupMember){
        Member member = groupMember.getMember();
        return new GroupMemberRes(
                groupMember.getId(),
                member.getId(),
                member.getName(),
                member.getRole(),
                member.getProfileImage(),
                groupMember.getPermission()
        );
    }
}
