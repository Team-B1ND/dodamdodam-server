package b1nd.dodam.domain.rds.group.event;

import b1nd.dodam.domain.rds.group.entity.Group;
import b1nd.dodam.domain.rds.group.enumeration.GroupPermission;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;

public record JoinMemberToGroupEvent(GroupPermission permission, Member member, Group group, ApprovalStatus status) {}