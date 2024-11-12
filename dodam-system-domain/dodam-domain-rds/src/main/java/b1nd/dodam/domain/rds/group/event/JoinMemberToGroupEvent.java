package b1nd.dodam.domain.rds.group.event;

import b1nd.dodam.domain.rds.group.entity.Group;
import b1nd.dodam.domain.rds.member.entity.Member;

public record JoinMemberToGroupEvent(Member member, Group group) {

}
