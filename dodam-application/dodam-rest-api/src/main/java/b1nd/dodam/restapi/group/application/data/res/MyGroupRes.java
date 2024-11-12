package b1nd.dodam.restapi.group.application.data.res;

import b1nd.dodam.domain.rds.group.entity.Group;
import b1nd.dodam.domain.rds.group.entity.GroupMember;
import b1nd.dodam.domain.rds.group.enumeration.GroupPermission;

import java.util.List;

public record MyGroupRes(
        Long id,
        String name,
        GroupPermission permission
) {
    static public List<MyGroupRes> of(List<GroupMember> groupMembers){
        return groupMembers.stream()
                .map(gm -> {
                    Group group = gm.getGroup();
                    return new MyGroupRes(group.getId(), group.getName(), gm.getPermission());
                })
                .toList();
    }
}
