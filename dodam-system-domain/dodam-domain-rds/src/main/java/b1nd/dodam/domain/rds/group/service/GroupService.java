package b1nd.dodam.domain.rds.group.service;

import b1nd.dodam.domain.rds.group.entity.Group;
import b1nd.dodam.domain.rds.group.exception.GroupNotFoundException;
import b1nd.dodam.domain.rds.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

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
