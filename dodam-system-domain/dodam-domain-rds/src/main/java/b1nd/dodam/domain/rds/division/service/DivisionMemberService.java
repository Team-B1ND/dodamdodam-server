package b1nd.dodam.domain.rds.division.service;

import b1nd.dodam.core.exception.global.InvalidPermissionException;
import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.division.entity.DivisionMember;
import b1nd.dodam.domain.rds.division.enumeration.DivisionPermission;
import b1nd.dodam.domain.rds.division.exception.DivisionMemberNotFoundException;
import b1nd.dodam.domain.rds.division.repository.DivisionMemberRepository;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DivisionMemberService {

    private final DivisionMemberRepository repository;

    public void modifyDivisionMembers(List<DivisionMember> divisionMembers, ApprovalStatus status) {
        for (DivisionMember divisionMember : divisionMembers) {
            divisionMember.modifyStatus(status);
        }
        repository.saveAll(divisionMembers);
    }

    public void saveWithBuild(Division division, Member member, ApprovalStatus status, DivisionPermission permission) {
        repository.save(DivisionMember.builder()
                .division(division)
                .member(member)
                .status(status)
                .permission(permission)
                .build()
        );
    }

    public void saveAllWithBuild(Division division, List<Member> members, ApprovalStatus status, DivisionPermission permission) {
        repository.saveAll(members
                .stream()
                .map(member -> DivisionMember.builder()
                    .division(division)
                    .member(member)
                    .status(status)
                    .permission(permission)
                    .build())
                .toList()
        );
    }

    public DivisionMember getById(Long id) {
        return repository.findById(id)
                .orElseThrow(DivisionMemberNotFoundException::new);
    }

    public List<DivisionMember> getByIds(List<Long> ids) {
        return repository.findAllById(ids);
    }

    public void deleteByIds(List<Long> idList){
        repository.deleteAllByIdInBatch(idList);
    }

    public void deleteByDivision(Division division) {
        repository.deleteAllByDivision(division);
    }

    public void validateIsAdminInDivision(Division division, Member member) {
        DivisionMember divisionMember = repository.findByDivisionAndMember(division, member);
        if (!DivisionPermission.isAdmin(divisionMember.getPermission())){
            throw new InvalidPermissionException();
        }
    }

    public DivisionMember getByDivisionAndMember(Division division, Member member) {
        return repository.findByDivisionAndMember(division, member);
    }

    public Long getCountByDivision(Division division, ApprovalStatus status) {
        return repository.countByDivisionAndStatus(division, status);
    }

    public List<DivisionMember> getByMember(Member member) {
        return repository.findByMember(member);
    }

    public DivisionMember getByDivisionAndMemberAndStatus(Division division, Member member, ApprovalStatus status) {
        return repository.findByDivisionAndMemberAndStatus(division, member, status);
    }

    public List<DivisionMember> getByDivisionAndStatus(Division division, ApprovalStatus status) {
        return repository.findByDivisionAndStatus(division, status);
    }
}
