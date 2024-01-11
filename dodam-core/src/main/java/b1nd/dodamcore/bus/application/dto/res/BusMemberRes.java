package b1nd.dodamcore.bus.application.dto.res;

import b1nd.dodamcore.bus.domain.entity.BusMember;

public record BusMemberRes(String memberId, String name, String email, String phone) {

    public static BusMemberRes createFromBusMember(BusMember busMember) {
        return new BusMemberRes(
                busMember.getStudent().getMember().getId(),
                busMember.getStudent().getMember().getName(),
                busMember.getStudent().getMember().getEmail(),
                busMember.getStudent().getMember().getPhone()
        );
    }
}
