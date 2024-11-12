package b1nd.dodam.restapi.group.application;

import b1nd.dodam.domain.rds.group.entity.Group;
import b1nd.dodam.domain.rds.group.entity.GroupMember;
import b1nd.dodam.domain.rds.group.enumeration.GroupPermission;
import b1nd.dodam.domain.rds.group.event.JoinMemberToGroupEvent;
import b1nd.dodam.domain.rds.group.service.GroupMemberService;
import b1nd.dodam.domain.rds.group.service.GroupService;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.group.application.data.req.ManageGroupReq;
import b1nd.dodam.restapi.group.application.data.res.GroupMemberRes;
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
    private final GroupMemberService groupMemberService;
    private final ApplicationEventPublisher eventPublisher;
    private final MemberAuthenticationHolder authenticationHolder;

    public Response organize(ManageGroupReq req){
        Group group = req.toEntity();
        groupService.save(group);
        publishOrganizeGroupEvent(group, GroupPermission.ADMIN, ApprovalStatus.ALLOWED);
        return Response.noContent("그룹 조직 성공");
    }

    public Response modify(Long id, ManageGroupReq req){
        Group group = getGroupByIdWithValidateAdmin(id);
        group.modify(req.name());
        groupService.save(group);
        return Response.noContent("그룹 정보 수정 성공");
    }

    public Response delete(Long id){
        Group group = getGroupByIdWithValidateAdmin(id);
        groupMemberService.deleteByGroup(group);
        groupService.delete(id);
        return Response.noContent("그룹 삭제 성공");
    }

    public Response exitFromGroupById(Long id){
        groupMemberService.deleteById(id);
        return Response.ok("그룹 내 멤버 추방 및 탈퇴");
    }

    public Response applyGroup(Long id){
        Group group = groupService.getById(id);
        publishOrganizeGroupEvent(group, GroupPermission.READER, ApprovalStatus.PENDING);
        return Response.ok("그룹에 가입 신청 성공");
    }

    public Response handleGroupApplication(Long id, ApprovalStatus status){
        GroupMember groupMember = groupMemberService.getById(id);
        groupMember.modifyStatus(status);
        groupMemberService.save(groupMember);
        return Response.ok("그룹 수락/거절/취소 성공");
    }

    @Transactional(readOnly = true)
    public ResponseData<List<MyGroupRes>> getMyGroups(ApprovalStatus status){
        List<GroupMember> groupMembers = groupMemberService.getByMemberAndStatus(authenticationHolder.current(), status);
        return ResponseData.ok("내 그룹 조회 성공", MyGroupRes.of(groupMembers));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<GroupMemberRes>> getGroupMemberByGroupId(Long id){
        Group group = groupService.getById(id);
        List<GroupMember> groupMembers = groupMemberService.getByGroup(group);
        return ResponseData.ok("그룹 내 멤버 조회 성공", GroupMemberRes.of(groupMembers));
    }

    private Group getGroupByIdWithValidateAdmin(Long id){
        Group group = groupService.getById(id);
        groupMemberService.validateIsAdminInGroup(group, authenticationHolder.current());
        return group;
    }

    private void publishOrganizeGroupEvent(Group group, GroupPermission permission, ApprovalStatus status){
        eventPublisher.publishEvent(
                new JoinMemberToGroupEvent(permission, authenticationHolder.current(), group, status));
    }
}
