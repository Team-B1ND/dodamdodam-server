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

    private final DivisionMemberRepository divisionMemberRepository;

    public void save(DivisionMember divisionMember) {
        divisionMemberRepository.save(divisionMember);
    }

    public DivisionMember getById(Long id) {
        return divisionMemberRepository.findById(id)
                .orElseThrow(DivisionMemberNotFoundException::new);
    }

    public void deleteById(Long id){
        divisionMemberRepository.deleteById(id);
    }

    public void deleteByDivision(Division division) {
        divisionMemberRepository.deleteAllByDivision(division);
    }

    public void validateIsAdminInDivision(Division division, Member member) {
        DivisionMember divisionMember = divisionMemberRepository.findByDivisionAndMember(division, member);
        if (!DivisionPermission.isAdmin(divisionMember.getPermission())){
            throw new InvalidPermissionException();
        }
    }

    public Long getCountByDivision(Division division, ApprovalStatus status) {
        return divisionMemberRepository.countByDivisionAndStatus(division, status);
    }

    public List<DivisionMember> getByMember(Member member) {
        return divisionMemberRepository.findByMember(member);
    }

    public DivisionMember getByDivisionAndMemberAndStatus(Division division, Member member, ApprovalStatus status) {
        return divisionMemberRepository.findByDivisionAndMemberAndStatus(division, member, status);
    }

    public List<DivisionMember> getByDivision(Division division) {
        return divisionMemberRepository.findByDivision(division);
    }
}
