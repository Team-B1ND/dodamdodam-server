package b1nd.dodam.restapi.group.application;

import b1nd.dodam.domain.rds.group.entity.Group;
import b1nd.dodam.domain.rds.group.entity.GroupMember;
import b1nd.dodam.domain.rds.group.event.JoinMemberToGroupEvent;
import b1nd.dodam.domain.rds.group.service.GroupService;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.group.application.data.req.ManageGroupReq;
import b1nd.dodam.restapi.group.application.data.res.MyGroupRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class GroupUseCase {
    private final GroupService groupService;
    private final ApplicationEventPublisher eventPublisher;
    private final MemberAuthenticationHolder authenticationHolder;

    public Response organize(ManageGroupReq req){
        Group group = req.toEntity();
        groupService.save(group);
        publishOrganizeGroupEvent(group);
        return Response.noContent("그룹 조직 성공");
    }

    public Response modify(Long id, ManageGroupReq req){
        Group group = getGroupByIdAndValidateAdmin(id);
        group.modify(req.name());
        groupService.save(group);
        return Response.noContent("그룹 정보 수정 성공");
    }

    public Response delete(Long id){
        Group group = getGroupByIdAndValidateAdmin(id);
        groupService.deleteGroupMemberByGroup(group);
        groupService.delete(id);
        return Response.noContent("그룹 삭제 성공");
    }

    @Transactional(readOnly = true)
    public ResponseData<List<MyGroupRes>> getMyGroups(){
        List<GroupMember> groupMembers = groupService.getGroupMemberByMember(authenticationHolder.current());
        return ResponseData.ok("내 그룹 조회 성공", MyGroupRes.of(groupMembers));
    }

    private Group getGroupByIdAndValidateAdmin(Long id){
        Group group = groupService.getById(id);
        groupService.validateIsAdminInGroup(group, authenticationHolder.current());
        return group;
    }

    private void publishOrganizeGroupEvent(Group group){
        eventPublisher.publishEvent(
                new JoinMemberToGroupEvent(authenticationHolder.current(), group));
    }
}
