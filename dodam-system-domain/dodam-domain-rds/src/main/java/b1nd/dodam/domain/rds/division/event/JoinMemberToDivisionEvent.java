package b1nd.dodam.domain.rds.division.event;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.division.enumeration.DivisionPermission;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;

public record JoinMemberToDivisionEvent(DivisionPermission permission, Member member, Division division, ApprovalStatus status) {}