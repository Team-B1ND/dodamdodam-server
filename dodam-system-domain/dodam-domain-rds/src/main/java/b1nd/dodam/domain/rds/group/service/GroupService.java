package b1nd.dodam.domain.rds.group.service;

import b1nd.dodam.core.exception.global.InvalidPermissionException;
import b1nd.dodam.domain.rds.group.entity.Group;
import b1nd.dodam.domain.rds.group.entity.GroupMember;
import b1nd.dodam.domain.rds.group.enumeration.GroupPermission;
import b1nd.dodam.domain.rds.group.exception.GroupNotFoundException;
import b1nd.dodam.domain.rds.group.repository.GroupMemberRepository;
import b1nd.dodam.domain.rds.group.repository.GroupRepository;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    public void save(Group group) {
        groupRepository.save(group);
    }

    public Group getById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(GroupNotFoundException::new);
    }

    public void delete(Long id) {
        groupRepository.deleteById(id);
    }



}
