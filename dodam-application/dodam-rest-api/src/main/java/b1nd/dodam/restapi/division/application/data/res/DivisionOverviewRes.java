package b1nd.dodam.restapi.division.application.data.res;

import b1nd.dodam.domain.rds.division.entity.Division;

import java.util.List;

public record DivisionOverviewRes(
        Long id,
        String name
) {
    public static List<DivisionOverviewRes> of(List<Division> divisions) {
        return divisions.stream()
                .map(division -> new DivisionOverviewRes(division.getId(), division.getName()))
                .toList();
    }
}
