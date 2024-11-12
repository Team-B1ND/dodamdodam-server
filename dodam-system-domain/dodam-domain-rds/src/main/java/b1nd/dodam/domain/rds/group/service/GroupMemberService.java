package b1nd.dodam.domain.rds.group.service;

import b1nd.dodam.core.exception.global.InvalidPermissionException;
import b1nd.dodam.domain.rds.group.entity.Group;
import b1nd.dodam.domain.rds.group.entity.GroupMember;
import b1nd.dodam.domain.rds.group.enumeration.GroupPermission;
import b1nd.dodam.domain.rds.group.exception.GroupNotFoundException;
import b1nd.dodam.domain.rds.group.repository.GroupMemberRepository;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;

    public void save(GroupMember groupMember) {
        groupMemberRepository.save(groupMember);
    }

    public GroupMember getById(Long id) {
        return groupMemberRepository.findById(id)
                .orElseThrow(GroupNotFoundException::new);
    }

    public void deleteById(Long id){
        groupMemberRepository.deleteById(id);
    }

    public void deleteByGroup(Group group) {
        groupMemberRepository.deleteAllByGroup(group);
    }

    public void validateIsAdminInGroup(Group group, Member member) {
        GroupMember groupMember = groupMemberRepository.findByGroupAndMember(group, member);
        if (!GroupPermission.isAdmin(groupMember.getPermission())){
            throw new InvalidPermissionException();
        }
    }

    public List<GroupMember> getByMemberAndStatus(Member member, ApprovalStatus status) {
        return groupMemberRepository.findByMemberAndStatus(member, status);
    }

    public List<GroupMember> getByGroup(Group group) {
        return groupMemberRepository.findByGroup(group);
    }
}
