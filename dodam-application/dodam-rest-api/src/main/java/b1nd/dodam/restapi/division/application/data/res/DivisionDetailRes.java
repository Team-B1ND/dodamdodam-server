package b1nd.dodam.restapi.division.application.data.res;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.division.entity.DivisionMember;
import b1nd.dodam.domain.rds.division.enumeration.DivisionPermission;

import java.util.Optional;

public record DivisionDetailRes(
        Long id,
        String divisionName,
        String description,
        DivisionPermission myPermission
) {
    static public DivisionDetailRes of(Division division, DivisionMember dm) {
        return new DivisionDetailRes(
                division.getId(),
                division.getName(),
                division.getDescription(),
                Optional.ofNullable(dm).map(DivisionMember::getPermission).orElse(null)
        );
    }
}
